package site.chatmanager.api.handler.impl;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import site.chatmanager.enums.Model;
import site.chatmanager.api.handler.ModelHandler;

import java.util.Map;

/**
 * DeepSeek模型处理器（处理DeepSeek特有的配置和请求格式）
 * 配置参数假设：
 * - deepseek_api_key：认证密钥（非统一的apiKey，体现模型自治）
 * - deepseek_model_id：模型ID（参数名自定义）
 */
public class DeepSeekHandler implements ModelHandler {

    @Override
    public String getApiUrl() {
        // DeepSeek的API地址（可从配置中心获取，此处硬编码示例）
        return "https://api.deepseek.com/v1/chat/completions";
    }

    @Override
    public RequestBody buildRequestBody(Map<String, Object> config, String question) {
        // 提取DeepSeek特有的配置参数（参数名与模型强相关，非顶层规定）
        String modelId = (String) config.get("deepseek_model_id");
        String escapedQuestion = question.replace("\"", "\\\""); // 转义处理

        // 构建符合DeepSeek API规范的请求体（包含其特有的参数结构）
        String json = String.format(
                "{\"model\":\"%s\",\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}",
                modelId, escapedQuestion
        );
        return RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
    }

    @Override
    public Headers getRequestHeaders(Map<String, Object> config) {
        // 提取DeepSeek的认证参数（假设配置中的键是deepseek_api_key）
        String apiKey = (String) config.get("deepseek_api_key");
        // 使用DeepSeek要求的认证头格式（Bearer Token）
        return Headers.of("Authorization", "Bearer " + apiKey);
    }

    @Override
    public Model getModel() {
        // 关联模型枚举，用于工厂映射
        return Model.DEEPSEEK;
    }
}