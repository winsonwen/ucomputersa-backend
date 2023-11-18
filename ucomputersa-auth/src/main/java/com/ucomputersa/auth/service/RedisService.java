package com.ucomputersa.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    public void saveUserToRedisWithExpiration(String partition, String key, Object object, long timeout, TimeUnit timeUnit) {
        String fullKey = partition + ":" + key;
        redisTemplate.opsForValue().set(fullKey, object);
        redisTemplate.expire(fullKey, timeout, timeUnit);
    }

    public <T> T getObjectFromRedis(String partition, String key, Class<T> clazz) {
        String fullKey = partition + ":" + key;

        return clazz.cast(redisTemplate.opsForValue().get(fullKey));
    }
}
