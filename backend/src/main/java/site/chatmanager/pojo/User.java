package site.chatmanager.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Long uid;
    private Integer role;
    private Integer status;
    private String avatar;
    private String nickname;
    private String account;
    private String email;
    private String cellphone;
    private LocalDateTime createAt;
    private LocalDateTime lastLoginAt;
}
