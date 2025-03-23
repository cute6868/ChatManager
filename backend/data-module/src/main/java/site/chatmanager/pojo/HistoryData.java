package site.chatmanager.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HistoryData {
    private Map<String, String> history;
}
