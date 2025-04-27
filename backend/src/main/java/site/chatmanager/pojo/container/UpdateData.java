package site.chatmanager.pojo.container;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateData {
    private String account;
    private String password;
    private String email;
    private String verifyCode;
    private String secondVerifyCode;    // 主要用来验证用户更改后的邮箱是否是他自己的
}
