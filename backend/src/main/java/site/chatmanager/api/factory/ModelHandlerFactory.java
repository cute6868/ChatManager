package site.chatmanager.api.factory;

import site.chatmanager.api.handler.ModelHandler;
import site.chatmanager.api.handler.impl.DeepSeekHandler;
import site.chatmanager.api.handler.impl.DouBaoHandler;
import site.chatmanager.enums.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型处理器工厂（通过Map映射实现O(1)查找）
 * 职责：维护模型枚举与处理器的映射关系，新增模型时只需注册新处理器
 */
public class ModelHandlerFactory {

    // 模型枚举到处理器的映射（初始化时注册支持的模型）
    private static final Map<Model, ModelHandler> HANDLER_MAP = new HashMap<>();

    static {
        // 注册DeepSeek处理器（模型自治，与参数名无关）
        HANDLER_MAP.put(Model.DEEPSEEK, new DeepSeekHandler());
        // 注册Doubao处理器（参数名可与DeepSeek完全不同）
        HANDLER_MAP.put(Model.DOUBAO, new DouBaoHandler());
    }

    /**
     * 获取模型对应的处理器（无任何条件判断，纯Map查找）
     * @param model 模型枚举
     * @return 模型处理器实例
     * @throws IllegalArgumentException 不支持的模型
     */
    public static ModelHandler getHandler(Model model) {
        ModelHandler handler = HANDLER_MAP.get(model);
        if (handler == null) {
            throw new IllegalArgumentException("不支持的模型: " + model);
        }
        return handler;
    }
}