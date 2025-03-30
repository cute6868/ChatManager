package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterData {
    private String account;
    private String password;
    private String email;
    private String verifyCode;
}
