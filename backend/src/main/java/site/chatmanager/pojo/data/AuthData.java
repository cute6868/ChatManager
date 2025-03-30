package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthData {
    private Integer uid;
    private Integer status;
    private Integer role;
    private String password;
}
