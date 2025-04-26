package site.chatmanager.service.universal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 设置默认过期时间为一天（用来控制从数据库加载到redis中的模型配置的存活时间）
    private static final long DEFAULT_EXPIRE_TIME = 24 * 60 * 60;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    // ====================== 普通的 key:value 操作，比如短暂的保存邮箱验证码 ======================
    /**
     * 设置键值对（无过期时间）
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置键值对（有过期时间）
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取键对应的值
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键值对
     * @param key 键
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }


    // ====================== key:zset 操作，主要用来记录用户登录令牌黑名单 ======================
    /**
     * 向ZSet中添加元素并设置过期时间
     * @param key ZSet的键
     * @param value 元素值（jti）
     * @param expirationTimestamp 绝对过期时间戳（毫秒）
     */
    public void addToZSetWithExpiration(String key, String value, long expirationTimestamp) {
        // 向ZSet中添加元素，分数为过期时间戳
        stringRedisTemplate.opsForZSet().add(key, value, expirationTimestamp);
        // 设置整个ZSet的过期时间为当前元素的过期时间
        stringRedisTemplate.expireAt(key, new Date(expirationTimestamp));
    }

    /**
     * 检查元素是否在ZSet中且未过期
     * @param key ZSet的键
     * @param value 元素值（jti）
     * @return 是否有效
     */
    public boolean isMemberOfZSetAndValid(String key, String value) {
        Double score = stringRedisTemplate.opsForZSet().score(key, value);
        return score != null && score > System.currentTimeMillis();
    }

    /**
     * 从ZSet中删除元素
     * @param key ZSet的键
     * @param value 元素值（jti）
     */
    public void removeFromZSet(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 清理ZSet中已过期的元素
     * @param key ZSet的键
     */
    public void cleanupExpiredFromZSet(String key) {
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, System.currentTimeMillis());
    }


    // ====================== 主要用来记录和管理用户的模型配置信息 ======================
    /**
     * 设置用户某个模型的某个配置项
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称，如 "DeepSeek", "DouBao", ...
     * @param field 字段名
     * @param value 字段值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(Long uid, String modelName, String field, String value, long timeout, TimeUnit unit) {
        String key = getKey(uid, modelName);
        stringRedisTemplate.opsForHash().put(key, field, value);
        setModelsConfigExpiration(uid, timeout, unit);
    }

    /**
     * 设置用户某个模型的某个配置项（使用默认过期时间）
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称，如 "deepseek", "doubao", "tongyi", "chatgpt"
     * @param field 字段名
     * @param value 字段值
     */
    public void set(Long uid, String modelName, String field, String value) {
        set(uid, modelName, field, value, DEFAULT_EXPIRE_TIME, DEFAULT_TIME_UNIT);
    }

    /**
     * 获取用户某个模型的某个配置项
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称
     * @param field 字段名
     * @return 字段值，不存在时返回 null
     */
    public String get(Long uid, String modelName, String field) {
        String key = getKey(uid, modelName);
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * 设置用户某个模型的所有配置项
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称
     * @param configMap 配置的键值对
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(Long uid, String modelName, Map<String, String> configMap, long timeout, TimeUnit unit) {
        String key = getKey(uid, modelName);
        stringRedisTemplate.opsForHash().putAll(key, configMap);
        setModelsConfigExpiration(uid, timeout, unit);
    }

    /**
     * 设置用户某个模型的所有配置项（使用默认过期时间）
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称
     * @param configMap 配置的键值对
     */
    public void set(Long uid, String modelName, Map<String, String> configMap) {
        set(uid, modelName, configMap, DEFAULT_EXPIRE_TIME, DEFAULT_TIME_UNIT);
    }

    /**
     * 获取用户某个模型的所有配置项
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称
     * @return 配置的键值对
     */
    public Map<Object, Object> getModelConfig(Long uid, String modelName) {
        String key = getKey(uid, modelName);
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 生成 Redis 键
     * @param uid 用户 ID（Long类型）
     * @param modelName 模型名称
     * @return 生成的键
     */
    private String getKey(Long uid, String modelName) {
        return uid + ":" + modelName; // Long自动转换为字符串
    }

    /**
     * 设置用户所有模型配置的过期时间
     * @param uid 用户 ID（Long类型）
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    private void setModelsConfigExpiration(Long uid, long timeout, TimeUnit unit) {
        // 生成匹配模式时，将Long类型的uid转换为字符串
        String pattern = uid + ":*";
        Iterable<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null) {
            for (String key : keys) {
                stringRedisTemplate.expire(key, timeout, unit);
            }
        }
    }

    /**
     * 删除用户所有模型配置
     * @param uid 用户 ID（Long类型）
     */
    public void delModelsConfig(Long uid) {
        // 生成匹配模式时，将Long类型的uid转换为字符串
        String pattern = uid + ":*";
        Iterable<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null) {
            for (String key : keys) {
                stringRedisTemplate.delete(key);
            }
        }
    }
}