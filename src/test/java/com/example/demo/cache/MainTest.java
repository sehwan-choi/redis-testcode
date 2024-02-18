package com.example.demo.cache;

import com.example.demo.RedisConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.example.demo.cache")
@Import(RedisConfig.class)
public class MainTest {

    @Autowired
    UserService service;

    @Autowired
    UserRedisService redisService;

    @Autowired
    StringRedisTemplate template;

    @BeforeEach
    void beforeEach() {
        deleteAllUser();
        service.init();
    }

    @Test
    void test() {
        User user = service.getUser(1L);
        System.out.println("user = " + user);
        User user1 = redisService.getUser(1L);
        System.out.println("user1 = " + user1);

        Assertions.assertThat(user).isEqualTo(user1);
    }

    @Test
    void test2() {
        User updateUser = service.updateUser(1L, "zzzz");
        System.out.println("updateUser = " + updateUser);
        User findUser = redisService.getUser(1L);
        System.out.println("findUser = " + findUser);

        Assertions.assertThat(updateUser).isEqualTo(findUser);
    }

    @Test
    void test3() {
        getAllUser();
        service.deleteUser(1L);
        Assertions.assertThatThrownBy(() -> redisService.getUser(1L)).isInstanceOf(RuntimeException.class);
    }

    void getAllUser() {
        service.getUser(1L);
        service.getUser(2L);
        service.getUser(3L);
    }

    void deleteAllUser() {
        service.deleteUser(1L);
        service.deleteUser(2L);
        service.deleteUser(3L);
        redisService.deleteUser(1L);
        redisService.deleteUser(2L);
        redisService.deleteUser(3L);
    }
}
