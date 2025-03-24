package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoreData {
    private String account;
    private String password;
    private String email;
    private String emailVerificationCode;
    private String cellphone;
    private String cellphoneVerificationCode;
}
