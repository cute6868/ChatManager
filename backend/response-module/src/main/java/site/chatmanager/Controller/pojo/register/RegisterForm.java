package site.chatmanager.Controller.pojo.register;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private String account;
    private String password;
    private String email;
    private String verificationCode;
}
