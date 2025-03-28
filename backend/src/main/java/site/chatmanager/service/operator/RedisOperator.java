package site.chatmanager.service.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisOperator {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 设置键值对，没有过期时间
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 设置键值对，有过期时间
    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取值
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 删除键值对
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }
}
