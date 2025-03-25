package site.chatmanager.pojo.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import site.chatmanager.pojo.models.*;

@Data
@NoArgsConstructor
public class ModelData {
    private DeepSeek deepSeek;
    private Doubao doubao;
    private HunYuan hunYuan;
    private SanLiuLingZhiNao sanLiuLingZhiNao;
    private TongYiQianWen tongYiQianWen;
    private WenXinYiYan wenXinYiYan;
    private XunFeiXingHuo xunFeiXingHuo;
    private ZhiPuAI zhiPuAI;
}
