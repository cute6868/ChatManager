package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DialogData {
    private String question;
    private List<Integer> modelIds;
}
