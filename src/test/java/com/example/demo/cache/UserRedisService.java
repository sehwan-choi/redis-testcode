package com.example.demo.cache;

import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, User> ops;

    private static final String USER_PREFIX = "user::";

    public void init() {
        ops.set(USER_PREFIX + "1", new User(1L, "choi", 10));
        ops.set(USER_PREFIX + "2", new User(2L, "lee", 15));
        ops.set(USER_PREFIX + "3", new User(3L, "park", 20));
    }

    public User getUser(Long id) {
        return Optional.ofNullable(ops.get(USER_PREFIX + id)).orElseThrow(() -> new RuntimeException());
    }

    public User updateUser(Long id, String name) {
        User user = Optional.ofNullable(ops.getAndDelete(USER_PREFIX + id)).orElseThrow(() -> new RuntimeException());
        user.setName(name);
        ops.set(String.valueOf(user.getId()), user);
        return user;
    }

    public void deleteUser(Long id) {
        ops.getAndDelete(USER_PREFIX + id);
    }
}
