package site.chatmanager.api.handler;

import okhttp3.Headers;
import okhttp3.RequestBody;
import site.chatmanager.enums.Model;

import java.util.Map;

/**
 * 模型处理器接口（核心抽象层）
 * 职责：定义模型交互的通用方法，具体实现由各模型自行处理
 * 设计原则：不假设任何配置参数名，由具体模型解析自己的配置
 */
public interface ModelHandler {

    /**
     * 获取模型的API地址（模型特有，如DeepSeek和Doubao的URL不同）
     * @return 模型API完整URL
     */
    String getApiUrl();

    /**
     * 构建模型请求体（解析模型特有的配置参数）
     * @param config 原始配置（键值对集合，模型自行解析所需参数）
     * @param question 用户问题
     * @return OkHttp请求体
     */
    RequestBody buildRequestBody(Map<String, Object> config, String question);

    /**
     * 获取模型特有的请求头（处理认证等差异化配置）
     * @param config 原始配置
     * @return 请求头集合（如DeepSeek用Bearer Token，Doubao用X-Doubao-Token）
     */
    Headers getRequestHeaders(Map<String, Object> config);

    /**
     * 关联模型枚举（用于工厂映射）
     * @return 模型枚举值（如Model.DEEPSEEK）
     */
    Model getModel();
}