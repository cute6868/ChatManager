package site.chatmanager.service.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

// 根配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelConfig {
    private DeepSeekConfig DeepSeek;
    private BaiduWenXinYiYanConfig BaiduWenXinYiYan;
    private AliyunTongYiQianWenConfig AliyunTongYiQianWen;
    private TencentHunYuanConfig TencentHunYuan;
    private ZhiPuAIConfig ZhiPuAI;
    private ByteDanceDoubaoConfig ByteDanceDoubao;
    private XunFeiXingHuoConfig XunFeiXingHuo;
    private SanLiuLingZhiNaoConfig SanLiuLingZhiNao;
}

// DeepSeek 配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class DeepSeekConfig {
    private String apiKey;
    private String model;
}

// 百度文心一言配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class BaiduWenXinYiYanConfig {
    private String apiKey;
    private String secretKey;
}

// 阿里云通义千问配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class AliyunTongYiQianWenConfig {
    private String apiKey;
}

// 腾讯混元配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class TencentHunYuanConfig {
    private String secretId;
    private String secretKey;
}

// 智谱 AI 配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class ZhiPuAIConfig {
    private String apiKey;
}

// 字节跳动豆包配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class ByteDanceDoubaoConfig {
    private String apiKey;
    private String endpointName;
    private String modelName;
}

// 讯飞星火配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class XunFeiXingHuoConfig {
    private String apiKey;
}

// 360 智脑配置类
@Data
@NoArgsConstructor
@AllArgsConstructor
class SanLiuLingZhiNaoConfig {
    private String apiKey;
}