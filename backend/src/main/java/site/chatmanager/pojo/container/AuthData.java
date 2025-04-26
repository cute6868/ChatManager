package site.chatmanager.pojo.container;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthData {
    private Long uid;
    private Integer status;
    private Integer role;
    private String password;
}
