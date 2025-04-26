package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import site.chatmanager.model.impl.DeepSeekCallStrategy;
import site.chatmanager.model.impl.DouBaoCallStrategy;
import site.chatmanager.model.ModelCallStrategy;
import site.chatmanager.model.enums.Model;
import site.chatmanager.pojo.universal.Result;
import site.chatmanager.pojo.chat.UserChatRequest;
import site.chatmanager.pojo.chat.UserChatResponse;
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

    // 线程池作为成员变量，初始化一次，长期复用
    private final ExecutorService executorService;

    // 存储模型对应的调用策略
    private final Map<Model, ModelCallStrategy> strategyMap = new HashMap<>();

    // 构造函数
    public ChatServiceImpl() {
        // 自定义线程池（精确控制参数）
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

    // 处理用户请求的方法，接收用户请求对象，返回调用大模型后的响应列表
    @Override
    public ResponseEntity<Result> processRequest(Long uid, UserChatRequest request) {

        // 非空校验
        String question = request.getQuestion();
        if (question == null || question.trim().isEmpty()) {
            Result result = Result.failure("问题为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        List<Integer> modelIds = request.getModelIds();
        if (modelIds == null || modelIds.isEmpty()) {
            Result result = Result.failure("模型选择为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        // 用户传过来的数据校验和处理完毕后，就将用户的问题写入到历史记录中（如果失败了，可以再次写入，一共有三次机会）
        int times = 3;
        while (times > 0) {
            if (recordService.addHistory(uid, question)) break;
            times--;
        }

        // 创建一个响应列表，用于存储最终所有的响应结果
        List<UserChatResponse> responses = new ArrayList<>();

        // 用于存储每个异步任务的 Future 对象，方便后续获取结果
        List<Future<UserChatResponse>> futures = new ArrayList<>();

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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
                modelConfig = redisService.getModelConfig(uid, model.getName());    // 加载模型配置到Redis数据库成功，再次从Redis数据库获取模型配置
            }

            // 将Map<Object, Object>类型的modelConfig转换为Map<String, String>类型
            Map<String, String> validModelConfig = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> entry : modelConfig.entrySet()) {
                validModelConfig.put(entry.getKey().toString(), entry.getValue().toString());
            }

            // 创建一个 Callable 任务，该任务会调用具体的大模型 API 并返回响应
            Callable<UserChatResponse> task = () -> {
                // 创建一个新的响应对象
                UserChatResponse response = new UserChatResponse();

                // 设置响应对象中的模型名称为当前正在调用的模型
                response.setModel(model.getName());

                try {
                    // 根据模型获取对应的调用策略并调用模型
                    ModelCallStrategy strategy = strategyMap.get(model);
                    if (strategy != null) {
                        String rawResponse = strategy.callModel(question, validModelConfig);
                        response.setResponse(rawResponse);      // 直接使用模型响应的原始JSON字符串
                    } else {
                        response.setResponse("不支持的模型: " + model.getName());
                    }
                } catch (Exception e) {
                    // 若出现异常，设置错误提示信息
                    response.setResponse("调用 " + model.getName() + " 模型时出现错误，请重试");
                    // 记录错误日志
                    log.error(e.getMessage());
                }
                return response;
            };
            // 将任务提交到线程池执行，并将返回的 Future 对象添加到 futures 列表中
            futures.add(executorService.submit(task));
        }

        // 遍历 futures 列表，获取每个任务的执行结果
        for (Future<UserChatResponse> future : futures) {
            try {
                // 阻塞等待任务完成并获取结果，将结果添加到响应列表中
                responses.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                // 若获取结果时出现异常，提示用户
                UserChatResponse errorResponse = new UserChatResponse();
                errorResponse.setModel("Error");
                errorResponse.setResponse("获取模型响应时出现错误，请重试");
                responses.add(errorResponse);
                // 记录错误日志
                log.error(e.getMessage());
            }
        }

        Result result = Result.success("处理成功", responses);
        return ResponseEntity.ok(result);
    }
}    