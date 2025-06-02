package site.chatmanager.model.pojo;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.NoArgsConstructor;

// ====================== 模型响应规范 ======================

// 1.正常响应数据
// {
//    "model": "豆包",
//    "response": {
//        "reasoning": "我现在需要解决的问题是6加12等于多少。首先，我应该回忆一下基本的加法运算。6加12，可以拆分成6加10再加2，这样计算起来更简单。6加10等于16，然后16加2就是18。或者直接数手指，6之后数12个数，7、8、9、10、11、12、13、14、15、16、17、18，这样数到第12个数就是18。所以答案应该是18。\n",
//        "answer": "\n18"
//    },
//    "code": 0
// }

// 2.模型调用不正常或其他错误
// {
//    "model": "豆包",
//    "response": "调用豆包模型时出现问题，请检查模型配置后重试"
//    "code": 1
// }

@Data
@NoArgsConstructor
public class UserChatResponse {
    private String model;       // 调用的具体大模型名称

    @JsonRawValue
    private String response;    // 大模型针对问题给出的响应

    private Integer code;      // 响应状态码
}