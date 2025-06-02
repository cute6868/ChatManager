package site.chatmanager.model.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonResponseFormatter {

    private static final Gson gson = new Gson();

    /**
     * 从火山引擎API的响应中提取content和reasoning_content，并格式化为新的JSON字符串
     * @param originalResponse 原始响应JSON字符串
     * @return 格式化后的JSON字符串，格式如下：
     *      {
     *          "reasoning": "原响应中reasoning_content的内容",
     *          "answer": "原响应中content的内容"
     *      }
     */
    public static String formatResponse(String originalResponse) {
        try {
            JsonObject jsonResponse = JsonParser.parseString(originalResponse).getAsJsonObject();

            // 提取content和reasoning_content
            String content = jsonResponse.getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();

            String reasoningContent = jsonResponse.getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("reasoning_content")
                    .getAsString();

            // 构建新的JSON结构
            JsonObject formattedResponse = new JsonObject();
            formattedResponse.addProperty("reasoning", reasoningContent);
            formattedResponse.addProperty("answer", content);

            return gson.toJson(formattedResponse);
        } catch (Exception e) {
            log.info("解析用户响应失败: " + e.getMessage());
            throw new IllegalArgumentException("无法解析的JSON格式", e);
        }
    }
}
