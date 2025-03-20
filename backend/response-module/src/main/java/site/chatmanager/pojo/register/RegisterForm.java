package site.chatmanager.pojo.register;

import lombok.Data;

@Data
public class RegisterForm {
    private String account;
    private String password;
    private String email;
    private String verificationCode;
}
