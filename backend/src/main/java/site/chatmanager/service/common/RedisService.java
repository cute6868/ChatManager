package site.chatmanager.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // ====================== key:value 操作 ======================
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

    // ====================== key:zset 操作 ======================
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

    // ====================== 其他操作 ======================
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
}