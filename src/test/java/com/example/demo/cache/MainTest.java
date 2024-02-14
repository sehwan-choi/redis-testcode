package com.example.demo.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class MainTest {

    @Autowired
    UserService service;

    @Autowired
    UserRedisService redisService;

    @Autowired
    StringRedisTemplate template;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        service.init();
        redisService.init();
    }

    @Test
    void test() throws JsonProcessingException {
        User user = service.getUser(1L);
        System.out.println("user = " + user);
        User user1 = redisService.getUser(1L);
        System.out.println("user1 = " + user1);
    }
}
