package site.chatmanager.pojo.chat;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserChatResponse {
    private String model;       // 调用的具体大模型名称

    @JsonRawValue
    private String response;    // 大模型针对问题给出的响应
}