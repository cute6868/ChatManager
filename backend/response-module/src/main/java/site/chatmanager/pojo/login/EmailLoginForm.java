package site.chatmanager.pojo.login;

import lombok.Data;

@Data
public class EmailLoginForm {
    private String email;
    private String verificationCode;
}
