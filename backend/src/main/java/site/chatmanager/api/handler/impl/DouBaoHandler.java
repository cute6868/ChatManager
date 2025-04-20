package site.chatmanager.api.handler.impl;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import site.chatmanager.enums.Model;
import site.chatmanager.api.handler.ModelHandler;

import java.util.Map;

/**
 * Doubao模型处理器（处理Doubao特有的多模态配置和请求格式）
 * 配置参数假设：
 * - doubao_access_token：认证令牌（参数名自定义，与DeepSeek不同）
 * - doubao_model_name：模型名称（非modelId，体现模型自治）
 */
public class DouBaoHandler implements ModelHandler {

    @Override
    public String getApiUrl() {
        // Doubao的API地址（可从配置中心获取，此处硬编码示例）
        return "https://api.doubao.com/v1/chat/completions";
    }

    @Override
    public RequestBody buildRequestBody(Map<String, Object> config, String question) {
        // 提取Doubao特有的配置参数（参数名与DeepSeek完全不同）
        String modelName = (String) config.get("doubao_model_name");
        // 假设问题包含文本和图片URL，格式为"问题|图片URL"
        String[] parts = question.split("\\|");
        String text = parts[0];
        String imageUrl = parts.length > 1 ? parts[1] : (String) config.get("doubao_default_image_url"); // 使用配置中的默认图片URL

        // 构建Doubao要求的多模态请求体（包含text和image_url）
        String json = String.format(
                "{\"model\":\"%s\",\"messages\":[{" +
                        "\"content\":[{" +
                        "\"text\":\"%s\",\"type\":\"text\"}," +
                        "{\"image_url\":{\"url\":\"%s\"},\"type\":\"image_url\"}]," +
                        "\"role\":\"user\"}]}",
                modelName, text, imageUrl
        );
        return RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
    }

    @Override
    public Headers getRequestHeaders(Map<String, Object> config) {
        // 提取Doubao的认证参数（假设配置中的键是doubao_access_token）
        String accessToken = (String) config.get("doubao_access_token");
        // 使用Doubao要求的认证头（不同于DeepSeek）
        return Headers.of("X-Doubao-Auth", accessToken);
    }

    @Override
    public Model getModel() {
        return Model.DOUBAO;
    }
}