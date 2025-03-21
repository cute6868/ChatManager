package site.chatmanager.Controller.pojo.login;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginForm {
    private String account;
    private String password;
}
