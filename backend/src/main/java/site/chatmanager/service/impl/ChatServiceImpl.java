package site.chatmanager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import site.chatmanager.api.ModelApiService;
import site.chatmanager.enums.Model;
import site.chatmanager.mapper.QueryMapper;
import site.chatmanager.pojo.data.DialogData;
import site.chatmanager.service.ChatService;
import site.chatmanager.pojo.Result;
import site.chatmanager.service.common.RedisService;
import site.chatmanager.utils.ModelsConigLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ModelsConigLoader modelConigLoader;

    private final ModelApiService modelApiService = new ModelApiService();

    @Override
    public ResponseEntity<Result> recommend(Long uid) {
        // 查询用户兴趣
        String interests = queryMapper.queryInterests(uid);

        // 调用AI模型，获取推荐结果

        // 返回结果
        Result result = Result.success();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> chat(Long uid, DialogData data) {
        try {
            // 1. 参数校验：用户问题
            String question = data.getQuestion();
            if (question == null || question.trim().isEmpty()) {
                return buildErrorResponse("请求错误，问题不能为空");
            }

            // 2. 处理用户选择的模型ID列表
            List<Integer> modelIds = data.getModelIds();
            if (modelIds == null || modelIds.isEmpty()) {
                return buildErrorResponse("请求错误，未选择任何模型");
            }

            // 去重+校验模型ID有效性（确保映射到正确的Model枚举）
            List<Model> validModels = modelIds.stream()
                    .distinct()
                    .map(id -> {
                        try {
                            return Model.fromId(id);
                        } catch (IllegalArgumentException e) {
                            //log.info("无效的模型ID: {}", id);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (validModels.isEmpty()) {
                return buildErrorResponse("请求错误，没有选择有效的模型ID");
            }

            // 3. 多模型配置加载与请求发送
            Flux<String> combinedFlux = Flux.fromIterable(validModels)
                    .flatMap(model -> {
                        // 从Redis获取模型配置
                        Map<Object, Object> config = redisService.getModelConfig(uid, model.getName());

                        if (config == null) {
                            // 配置不存在，调用加载器
                            boolean loadSuccess = modelConigLoader.load(uid, model.getName());
                            if (!loadSuccess) {
                                return Flux.error(new RuntimeException("模型 " + model + " 配置加载失败"));
                            }
                            // 重新获取配置
                            config = redisService.getModelConfig(uid, model.getName());
                        }

                        // 将Object类型配置转换为ModelHandler所需的Map<String, Object>
                        Map<String, Object> handlerConfig = new HashMap<>();
                        config.forEach((k, v) -> handlerConfig.put(k.toString(), v));

                        // 发送模型请求，并为每个响应数据块添加模型ID标识
                        return modelApiService.sendStreamRequest(model, question, handlerConfig)
                                .map(chunk -> "[" + model.getModelId() + "] " + chunk);
                    });

            // 4. 封装流式响应
            StreamingResponseBody responseBody = outputStream -> {
                combinedFlux.subscribe(
                        chunk -> {
                            try {
                                outputStream.write(chunk.getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                log.info("流式传输错误: {}", e.getMessage());
                            }
                        },
                        error -> {
                            try {
                                log.info("模型请求出错: {}", error.getMessage());
                                // 将错误信息写入输出流，发送给用户
                                outputStream.write("请求错误，请检查模型配置是否正确".getBytes());
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                log.info("关闭输出流时出错: {}", e.getMessage());
                            }
                        },
                        () -> log.info("所有模型响应完成")
                );
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(responseBody);
        } catch (Exception e) {
            log.info("聊天服务发生错误: {}", e.getMessage(), e);
            return buildErrorResponse("请检查模型配置是否正确，然后重试");
        }
    }

    private ResponseEntity<StreamingResponseBody> buildErrorResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_PLAIN)
                .body(outputStream -> {
                    try {
                        outputStream.write(errorMessage.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        log.error("处理错误响应时出错: {}", e.getMessage());
                    }
                });
    }
}