package com.example.demo.string_redis_template;

import com.example.demo.TestObject;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

@SpringBootTest
public class ValueOperationTest {

    //     특정 오퍼레이션 직접 주입
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> ops;

    @BeforeEach
    void beforeEach() {
        ops.set("key1", "a");
        ops.set("key2", "b");
        ops.set("key3", "c");
        ops.set("key4", "d");
        ops.set("key5", "e");
        ops.set("key6", "f");
    }

    @AfterEach
    void afterEach() {
        ops.getAndDelete("key1");
        ops.getAndDelete("key2");
        ops.getAndDelete("key3");
        ops.getAndDelete("key4");
        ops.getAndDelete("key5");
        ops.getAndDelete("key6");
    }

    @Test
    void test() {
        String key1 = ops.get("key1");
        String key2 = ops.get("key2");
        String key3 = ops.get("key3");
        String key4 = ops.get("key4");
        String key5 = ops.get("key5");
        String key6 = ops.get("key6");
        String key7 = ops.get("key7");

        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);
        System.out.println("key3 = " + key3);
        System.out.println("key4 = " + key4);
        System.out.println("key5 = " + key5);
        System.out.println("key6 = " + key6);
        System.out.println("key7 = " + key7);

        Assertions.assertThat(key1).isEqualTo("a");
        Assertions.assertThat(key2).isEqualTo("b");
        Assertions.assertThat(key3).isEqualTo("c");
        Assertions.assertThat(key4).isEqualTo("d");
        Assertions.assertThat(key5).isEqualTo("e");
        Assertions.assertThat(key6).isEqualTo("f");
        Assertions.assertThat(key7).isNull();
    }

    @Test
    void test2() throws InterruptedException {
        String key1 = ops.getAndExpire("key1", Duration.ofMillis(1000));
        System.out.println("key1 = " + key1);

        Thread.sleep(500);
        String key1_middle = ops.get("key1");
        System.out.println("key1_middle = " + key1_middle);

        Thread.sleep(500);
        String expired = ops.get("key1");
        System.out.println("expired = " + expired);

        Assertions.assertThat(key1).isEqualTo("a");
        Assertions.assertThat(key1_middle).isEqualTo("a");
        Assertions.assertThat(expired).isNull();
    }

    @Test
    void test3() {
        String value = ops.get("key1");
        System.out.println("value = " + value);

        value = "update!";

        ops.set("key1", value);
        String update = ops.get("key1");
        System.out.println("update = " + update);

        Assertions.assertThat(update).isEqualTo("update!");
    }
}
