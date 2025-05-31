package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.chatmanager.exception.CustomException;
import site.chatmanager.model.impl.DeepSeekCallStrategy;
import site.chatmanager.model.impl.DouBaoCallStrategy;
import site.chatmanager.model.ModelCallStrategy;
import site.chatmanager.model.enums.Model;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.model.pojo.UserChatRequest;
import site.chatmanager.model.pojo.UserChatResponse;
import site.chatmanager.service.ChatService;
import site.chatmanager.service.universal.RecordService;
import site.chatmanager.service.universal.RedisService;
import site.chatmanager.model.utils.ModelConfigLoader;

import java.util.*;
import java.util.concurrent.*;


@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RecordService recordService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ModelConfigLoader modelConfigLoader;

    // ====================== 线程池(Start) ======================
    /*
     *   当用户发起请求，调用processRequest方法时，会遍历用户选择的模型
     *   对于每个大模型，要为它创建一个Callable任务，该任务的作用是调用具体的大模型API并返回响应
     *   通过 completionService.submit(task) 将任务提交到线程池 executorService 中执行
     *   线程池会根据自身的配置来管理和调度这些任务，决定是立即执行任务，还是将任务放入队列等待执行，或者创建新线程来执行任务。
     *   这样可以避免为每个任务都创建新线程带来的资源开销和性能问题，实现线程的复用
     */

    // 准备容器
    private final ExecutorService executorService;                              // 线程池容器
    private final Map<Model, ModelCallStrategy> strategyMap = new HashMap<>();  // 用来存储模型对应的调用策略的容器

    // 在构造函数中完成初始化（向容器里面填入内容）
    public ChatServiceImpl() {

        // 创建线程池
        executorService = new ThreadPoolExecutor(
                // 设置参数
                4,                                          // 核心线程数（根据CPU核心数调整，如果4核心就写4）
                8,                                          // 最大线程数（根据系统负载上限进行设置）
                60, TimeUnit.SECONDS,                       // 空闲线程存活时间
                new LinkedBlockingQueue<>(30)       // 任务队列（默认为无界队列，这里设置队列容量为30）
        );

        // 初始化策略映射
        strategyMap.put(Model.DEEPSEEK, new DeepSeekCallStrategy());
        strategyMap.put(Model.DOUBAO, new DouBaoCallStrategy());
    }

    // ====================== 线程池(End) ======================

    // 处理用户请求的方法，接收用户请求对象，返回调用大模型后的响应列表
    @Override
    public SseEmitter processRequest(Long uid, UserChatRequest request) {

        // 创建一个 SseEmitter 对象，用于发送 SSE 事件
        SseEmitter emitter = new SseEmitter();

        // 非空校验
        String question = request.getQuestion();
        if (question == null || question.trim().isEmpty()) {
            Result result = Result.failure("问题为空");
            sendRejectionMsg(emitter, result);  // 其他线程：发送拒绝消息，并断开 SSE 连接
            return emitter;                     // 主线程：返回 emitter 对象，与前端建立 SSE 连接，主线程完成这个操作后，就"结束生涯"被 Tomcat 回收了
        }
        List<Integer> modelIds = request.getModelIds();
        if (modelIds == null || modelIds.isEmpty()) {
            Result result = Result.failure("模型选择为空");
            sendRejectionMsg(emitter, result);
            return emitter;
        }

        // 模型idl列表去重复
        List<Model> models = modelIds
                .stream()                   // 将modelIds流转换为流
                .distinct()                 // 去重
                .map(Model::fromId)         // 将每个modelId映射为对应的Model枚举，无效的modelId会被映射为null
                .filter(Objects::nonNull)   // 过滤掉无效的模型
                .toList();                  // 将流转换为列表
        if (models.isEmpty()) {
            Result result = Result.failure("没有检测到有效的模型");
            sendRejectionMsg(emitter, result);
            return emitter;
        }

        // 用户传过来的数据校验和处理完毕后，就将用户的问题写入到历史记录中（如果失败了，可以再次写入，一共有三次机会）
        int times = 3;
        while (times > 0) {
            if (recordService.addHistory(uid, question)) break;
            times--;
        }

        // executorService 是一个线程池，负责实际执行任务
        // CompletionService 是包装 executorService 的线程池，本身并不执行任务，只是负责管理任务的提交和结果的获取
        // 让 CompletionService 利用线程池的资源来管理任务，可以方便地按任务完成顺序获取结果，而不是按照任务提交的顺序获取结果
        CompletionService<UserChatResponse> completionService = new ExecutorCompletionService<>(executorService);

        // 用于存储每个异步任务的 Future 对象，方便后续获取结果
        List<Future<?>> futures = new ArrayList<>();

        // 遍历用户选择的模型
        for (Model model : models) {
            // 从数据库中获取模型配置
            // 先从Redis数据库获取模型配置
            Map<Object, Object> modelConfig = redisService.getModelConfig(uid, model.getName());
            if (modelConfig == null || modelConfig.isEmpty()) {
                // Redis数据库中没有该模型的配置，尝试加载MySQL数据库的模型配置到Redis数据库中
                boolean isSuccessful = modelConfigLoader.load(uid, model.getName());
                if (!isSuccessful) {
                    // 加载模型配置到Redis数据库失败，无法调用API，很可能是模型配置错误
                    Result result = Result.failure("请检查模型配置是否正确");
                    sendRejectionMsg(emitter, result);
                    return emitter;
                }
                modelConfig = redisService.getModelConfig(uid, model.getName());    // 加载模型配置到Redis数据库成功，再次从Redis数据库获取模型配置
            }

            // 将Map<Object, Object>类型的modelConfig转换为Map<String, String>类型
            Map<String, String> validModelConfig = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> entry : modelConfig.entrySet()) {
                validModelConfig.put(entry.getKey().toString(), entry.getValue().toString());
            }

            // 创建一个 Callable 任务，该任务内容是调用具体的大模型 API 并返回响应
            Callable<UserChatResponse> task = () -> {
                // 创建一个新的响应对象
                UserChatResponse response = new UserChatResponse();

                // 设置响应对象中的模型名称为当前正在调用的模型
                response.setModel(model.getAlias());

                try {
                    // 根据模型获取对应的调用策略并调用模型
                    ModelCallStrategy strategy = strategyMap.get(model);
                    if (strategy != null) {
                        String rawResponse = strategy.callModel(question, validModelConfig);
                        response.setResponse(rawResponse);  // 直接使用模型响应的原始JSON字符串
                        response.setCode(0);                // 设置响应代码为0，表示成功
                    } else {
                        response.setResponse("\"不支持的模型: " + model.getAlias() + "\"");  // 因为用了生的字符串，所以要添加转义的双引号，下面同理
                        response.setCode(1);    // 设置响应代码为1，表示失败
                    }
                } catch (Exception e) {
                    // 若出现异常，设置错误提示信息
                    response.setResponse("\"调用 " + model.getAlias() + " 模型时出现问题，请检查模型配置后重试\"");
                    response.setCode(1);        // 设置响应代码为1，表示失败
                    // 记录错误日志
                    log.error(e.getMessage());
                }
                return response;
            };
            // 将任务提交到线程池执行，并将返回的 Future 对象添加到 futures 列表中
            futures.add(completionService.submit(task));
        }

        // 启动一个新线程处理任务结果，避免阻塞主线程
        new Thread(() -> {
            try {
                for (int i = 0; i < models.size(); i++) {
                    // 获取已经完成的任务结果
                    UserChatResponse response = completionService.take().get();
                    // 发送 SSE 事件，将获取结果发送给前端
                    emitter.send(SseEmitter.event().data(response));
                }
                // 所有结果发送完毕，结束 SSE 连接
                emitter.complete();
            } catch (Exception e) {
                // 若发送时出现异常，则结束 SSE 连接并发送错误
                emitter.completeWithError(new CustomException("出现异常，请重试"));
                // 记录错误日志
                log.error(e.getMessage());
            }
        }).start();

        // 主线程返回 SseEmitter 对象给客户端，建立 SSE 连接
        return emitter;
    }

    // 发送拒绝信息
    private void sendRejectionMsg(SseEmitter emitter, Result result) {
        try {
            // 发送 SSE 事件，携带要告诉用户的信息
            emitter.send(SseEmitter.event().data(result));
            // 结束 SSE 连接
            emitter.complete();
        } catch (Exception e) {
            // 若发送时出现异常，则结束 SSE 连接并发送错误
            emitter.completeWithError(new CustomException("出现异常，请重试"));
            // 记录错误日志
            log.error(e.getMessage());
        }
    }
}    