package site.chatmanager.model.impl;

import okhttp3.*;
import site.chatmanager.model.ModelCallStrategy;
import site.chatmanager.model.utils.JsonResponseFormatter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeepSeekCallStrategy implements ModelCallStrategy {
    private static final String API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))     // 保持10个空闲连接，存活5分钟
            .build();

    @Override
    public String callModel(String question, Map<String, String> modelConfig) throws IOException {
        // 构建请求体
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "{" +
                "\"model\": \"" + modelConfig.get("model") + "\"," +
                "\"messages\": [" +
                "{\"role\": \"system\",\"content\": \"你是人工智能助手.\"}," +
                "{\"role\": \"user\",\"content\": \"" + question + "\"}" +
                "]" +
                "}");

        // 构建HTTP请求
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + modelConfig.get("apiKey"))
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String originalResponse = response.body().string();
                return JsonResponseFormatter.formatResponse(originalResponse);
            } else {
                throw new IOException("调用 DeepSeek 模型时返回非成功状态码: " + response.code());
            }
        }
    }
}
