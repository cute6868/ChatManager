package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailData {
    private String email;
    private String verifyCode;
}
