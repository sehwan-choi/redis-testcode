package com.example.demo.string_redis_template;

import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Iterator;
import java.util.Set;

@SpringBootTest(classes = MainTest.class)
public class ZSetOperationTest {

    @Resource(name = "stringRedisTemplate")
    ZSetOperations<String, String> ops;


//    @Test
//    void init() {
//        ops.add("1", "user1", 10);
//        ops.add("1", "user2", 60);
//        ops.add("1", "user3", 40);
//        ops.add("1", "user4", 30);
//        ops.add("1", "user5", 50);
//        ops.add("1", "user6", 20);
//    }
//
//    @Test
//    void remove() {
//        ops.remove("1", "user1");
//        ops.remove("1", "user2");
//        ops.remove("1", "user3");
//        ops.remove("1", "user4");
//        ops.remove("1", "user5");
//        ops.remove("1", "user6");
//    }

    @BeforeEach
    void beforeEach() {
        ops.add("1", "user1", 10);
        ops.add("1", "user6", 20);
        ops.add("1", "user4", 30);
        ops.add("1", "user3", 40);
        ops.add("1", "user5", 50);
        ops.add("1", "user2", 60);
    }

    @AfterEach
    void afterEach() {
        ops.remove("1", "user1");
        ops.remove("1", "user2");
        ops.remove("1", "user3");
        ops.remove("1", "user4");
        ops.remove("1", "user5");
        ops.remove("1", "user6");
    }

    @Test
    void test() {
        Set<String> range = ops.range("1", 0, 3);
        Iterator<String> iterator = range.iterator();
        String next = iterator.next();
        String next2 = iterator.next();
        String next3 = iterator.next();
        String next4 = iterator.next();
        boolean hasNext = iterator.hasNext();

        Assertions.assertThat(next).isEqualTo("user1");
        Assertions.assertThat(next2).isEqualTo("user6");
        Assertions.assertThat(next3).isEqualTo("user4");
        Assertions.assertThat(next4).isEqualTo("user3");
        Assertions.assertThat(hasNext).isFalse();
    }

    @Test
    void test2() {
        Set<String> reverseRange = ops.reverseRange("1", 0, 3);
        Iterator<String> iterator = reverseRange.iterator();
        String next = iterator.next();
        String next2 = iterator.next();
        String next3 = iterator.next();
        String next4 = iterator.next();
        boolean hasNext = iterator.hasNext();

        Assertions.assertThat(next).isEqualTo("user2");
        Assertions.assertThat(next2).isEqualTo("user5");
        Assertions.assertThat(next3).isEqualTo("user3");
        Assertions.assertThat(next4).isEqualTo("user4");
        Assertions.assertThat(hasNext).isFalse();
    }
}
