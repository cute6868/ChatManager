package site.chatmanager.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RecommendData {
    private Map<String, String> recommend;
}
