package site.chatmanager.service.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatForm {
    // 等待解决，用户选择的模型列表

    // 用户问题
    private String question;
}
