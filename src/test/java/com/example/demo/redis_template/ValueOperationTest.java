package com.example.demo.redis_template;

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
    @Resource(name = "redisTemplate")
    private ValueOperations<String, TestObject> ops;



//    @Test
//    void init() {
//        ops.set("key1", "value1");
//        ops.set("key1", "value2");
//        ops.set("key3", "value3");
//        ops.set("key4", "value4");
//        ops.set("key5", "value5");
//        ops.set("key6", "value6");
//    }

    @BeforeEach
    void beforeEach() {
        ops.set("key1", new TestObject(1, "a"));
        ops.set("key2", new TestObject(2, "b"));
        ops.set("key3", new TestObject(3, "c"));
        ops.set("key4", new TestObject(4, "d"));
        ops.set("key5", new TestObject(5, "e"));
        ops.set("key6", new TestObject(6, "f"));
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
        TestObject key1 = ops.get("key1");
        TestObject key2 = ops.get("key2");
        TestObject key3 = ops.get("key3");
        TestObject key4 = ops.get("key4");
        TestObject key5 = ops.get("key5");
        TestObject key6 = ops.get("key6");
        TestObject key7 = ops.get("key7");

        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);
        System.out.println("key3 = " + key3);
        System.out.println("key4 = " + key4);
        System.out.println("key5 = " + key5);
        System.out.println("key6 = " + key6);
        System.out.println("key7 = " + key7);

        Assertions.assertThat(key1).isEqualTo(new TestObject(1, "a"));
        Assertions.assertThat(key2).isEqualTo(new TestObject(2, "b"));
        Assertions.assertThat(key3).isEqualTo(new TestObject(3, "c"));
        Assertions.assertThat(key4).isEqualTo(new TestObject(4, "d"));
        Assertions.assertThat(key5).isEqualTo(new TestObject(5, "e"));
        Assertions.assertThat(key6).isEqualTo(new TestObject(6, "f"));
        Assertions.assertThat(key7).isNull();
    }

    @Test
    void test2() throws InterruptedException {
        TestObject key1 = ops.getAndExpire("key1", Duration.ofMillis(1000));
        System.out.println("key1 = " + key1);

        Thread.sleep(500);
        TestObject key1_middle = ops.get("key1");
        System.out.println("key1_middle = " + key1_middle);

        Thread.sleep(500);
        TestObject expired = ops.get("key1");
        System.out.println("expired = " + expired);

        Assertions.assertThat(key1).isEqualTo(new TestObject(1, "a"));
        Assertions.assertThat(key1_middle).isEqualTo(new TestObject(1, "a"));
        Assertions.assertThat(expired).isNull();
    }

    @Test
    void test3() {
        TestObject key1 = ops.get("key1");
        System.out.println("key1 = " + key1);

        key1.setName("update!");

        ops.set("key1", key1);
        TestObject update = ops.get("key1");
        System.out.println("update = " + update);

        Assertions.assertThat(update.getName()).isEqualTo("update!");
    }
}
