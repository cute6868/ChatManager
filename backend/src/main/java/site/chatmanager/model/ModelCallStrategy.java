package site.chatmanager.model;

import java.io.IOException;
import java.util.Map;

// 模型调用策略接口
public interface ModelCallStrategy {

    // 调用模型
    String callModel(String question, Map<String, String> modelConfig) throws IOException;

}
