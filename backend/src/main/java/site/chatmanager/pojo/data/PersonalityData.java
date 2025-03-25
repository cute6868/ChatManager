package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PersonalityData {
    private String nickname;
    private String avatar;
    private List<String> interests;
}
