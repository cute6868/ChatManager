package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RecordData {
    private String question;
    private LocalDateTime time;
    private Integer sequence_num;
}
