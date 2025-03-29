package site.chatmanager.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(value = {"uid", "role", "status", "password", "emailVerificationCode", "cellphoneVerificationCode"}, ignoreUnknown = true)
@Data
@NoArgsConstructor
public class CoreData {
    private Long uid;
    private Integer role;
    private Integer status;
    private String account;
    private String password;
    private String email;
    private String emailVerificationCode;
    private String cellphone;
    private String cellphoneVerificationCode;
}
