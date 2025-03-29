package site.chatmanager.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 在序列化时忽略 "interests" 字段，反序列化时接受 "interests" 字段
@JsonIgnoreProperties(value = {"interests"}, ignoreUnknown = true)
@Data
@NoArgsConstructor
public class PersonalityData {
    private String nickname;
    private String avatar;
    private List<String> interests;
}
