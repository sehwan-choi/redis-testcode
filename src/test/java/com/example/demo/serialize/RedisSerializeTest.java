package com.example.demo.serialize;

import com.example.demo.RedisConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
@Import(RedisConfig.class)
@SpringBootApplication(scanBasePackages = "com.example.demo.serialize")
public class RedisSerializeTest {


    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final String TEST_KEY = "test_key3";
    private static final String TEST_VALUE = "test_value3";

    @BeforeEach
    void beforeEach() {
        redisTemplate.delete(TEST_KEY);
        stringRedisTemplate.delete(TEST_KEY);
    }

    @Test
    @DisplayName("redisTemplate에서 set redisTemplate에서 get")
    void test2() {
        redisTemplate.opsForValue().set(TEST_KEY, TEST_VALUE);
        String value = (String)redisTemplate.opsForValue().get(TEST_KEY);
        Assertions.assertThat(value).isEqualTo(TEST_VALUE);
    }

    @Test
    @DisplayName("stringRedisTemplate에서 set stringRedisTemplate에서 get")
    void test3() {
        stringRedisTemplate.opsForValue().set(TEST_KEY, TEST_VALUE);
        String value = stringRedisTemplate.opsForValue().get(TEST_KEY);
        Assertions.assertThat(value).isEqualTo(TEST_VALUE);
    }

}
