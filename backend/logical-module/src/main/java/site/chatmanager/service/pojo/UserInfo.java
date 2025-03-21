package site.chatmanager.service.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String nickname;
    private String avatar;
    private String account;
    private String password;
    private String email;
    private String phone;
}
