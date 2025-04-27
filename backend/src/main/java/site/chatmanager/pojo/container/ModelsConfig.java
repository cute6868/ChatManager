package site.chatmanager.pojo.container;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModelsConfig {

    @JsonRawValue
    private String config;

}
