package site.chatmanager.pojo.container;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginData {
    Long uid;
    Integer role;
    LocalDateTime time;
    String token;
}
