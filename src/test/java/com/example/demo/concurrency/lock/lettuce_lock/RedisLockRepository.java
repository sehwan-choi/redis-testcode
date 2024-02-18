package com.example.demo.concurrency.lock.lettuce_lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String,String> redisTemplate;

    private static final String LOCK_PREFIX = "lock::";

    public Boolean lock(String key) { //lock 설정
        return redisTemplate
                .opsForValue()
                .setIfAbsent(LOCK_PREFIX + key, "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(String key) { //lock 해제
        return redisTemplate.delete(LOCK_PREFIX + key);
    }
}
