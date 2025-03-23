package site.chatmanager.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DialogData {
    private List<String> models;
    private String question;
}
