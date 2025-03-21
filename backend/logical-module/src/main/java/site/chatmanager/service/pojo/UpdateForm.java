package site.chatmanager.service.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
    private UserInfo userInfo;
    private ModelConfig modelConfig;
}