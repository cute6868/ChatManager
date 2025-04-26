package site.chatmanager.pojo.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserChatRequest {
    private String question;            // 用户提出的问题
    private List<Integer> modelIds;     // 用户选择要调用的大模型 ID 列表
}