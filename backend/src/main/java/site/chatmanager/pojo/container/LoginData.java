package site.chatmanager.pojo.container;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginData {
    //Long uid;
    String uid;  // 补丁：Java后端返回的Long类型uid会转为JS前端的Number类型uid，会丢失精度，由于给前端String类型的uid不影响业务，因此这里使用String类型uid
    Integer role;
    LocalDateTime time;
    String token;
}
