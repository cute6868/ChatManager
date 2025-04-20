package site.chatmanager.api;

import okhttp3.*;
import reactor.core.publisher.Flux;
import site.chatmanager.api.factory.ModelHandlerFactory;
import site.chatmanager.api.handler.ModelHandler;
import site.chatmanager.enums.Model;

import java.io.IOException;
import java.util.Map;

/**
 * 模型API服务类（顶层通用逻辑，不涉及任何模型特有的参数解析）
 * 职责：发送HTTP请求、处理流式响应，其余细节由模型处理器处理
 */
public class ModelApiService {

    // OkHttp客户端（线程安全，内置连接池优化性能）
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(10, 5, java.util.concurrent.TimeUnit.MINUTES)) // 维护10个空闲连接，5分钟超时
            .build();

    /**
     * 发送模型请求并获取流式响应（核心方法）
     *
     * @param model    模型枚举
     * @param question 用户问题（可能包含模型特有的格式，如Doubao的"文本|图片URL"）
     * @param config   原始模型配置（键值对集合，由模型处理器自行解析）
     * @return 流式响应（每行数据）
     */
    public Flux<String> sendStreamRequest(Model model, String question, Map<String, Object> config) {
        try {
            // 1. 通过工厂获取模型处理器（解耦模型与实现）
            ModelHandler handler = ModelHandlerFactory.getHandler(model);

            // 2. 调用处理器获取模型特有的请求信息
            String apiUrl = handler.getApiUrl();
            Headers headers = handler.getRequestHeaders(config);
            RequestBody requestBody = handler.buildRequestBody(config, question);

            // 3. 构建OkHttp请求（通用逻辑，不关心参数名）
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .headers(headers) // 应用模型特有的请求头（如认证信息）
                    .post(requestBody)
                    .build();

            // 4. 发送异步请求并处理流式响应（与模型无关）
            return Flux.create(sink -> {
                OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        sink.error(e); // 请求失败时发送错误信号
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody body = response.body()) {
                            if (!response.isSuccessful()) {
                                sink.error(new IOException("模型响应错误: 状态码 " + response.code()));
                                return;
                            }
                            // 逐行读取流式响应（通用处理，模型无关）
                            String line;
                            while ((line = body.source().readUtf8Line()) != null) {
                                sink.next(line); // 发送每行数据到流中
                            }
                            sink.complete(); // 流结束
                        }
                    }
                });
            });

        } catch (Exception e) {
            return Flux.error(e); // 异常处理
        }
    }
}