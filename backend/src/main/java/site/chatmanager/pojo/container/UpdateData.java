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
}
