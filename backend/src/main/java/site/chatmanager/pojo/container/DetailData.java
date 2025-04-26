package site.chatmanager.pojo.container;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DetailData {
    private Long uid;
    private String nickname;
    private String avatar;
    private LocalDateTime createAt;

    private String account;
    private String email;
    private String cellphone;

    private Integer status;
    private Integer role;
    private LocalDateTime lastLoginAt;
}
