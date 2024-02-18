package com.example.demo.string_redis_template;

import com.example.demo.RedisConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.example.demo.string_redis_template")
@Import(RedisConfig.class)
public class MainTest {
}
