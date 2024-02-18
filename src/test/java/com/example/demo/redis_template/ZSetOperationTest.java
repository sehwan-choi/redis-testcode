package com.example.demo.redis_template;

import com.example.demo.TestObject;
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

    @Resource(name = "redisTemplate")
    ZSetOperations<String, TestObject> ops;

    @BeforeEach
    void beforeEach() {
        ops.add("1", new TestObject(1,"user1", 10), 10);
        ops.add("1", new TestObject(1,"user6", 20), 20);
        ops.add("1", new TestObject(1,"user4", 30), 30);
        ops.add("1", new TestObject(1,"user3", 40), 40);
        ops.add("1", new TestObject(1,"user5", 50), 50);
        ops.add("1", new TestObject(1,"user2", 60), 60);
    }

    @AfterEach
    void afterEach() {
        ops.remove("1", new TestObject(1,"user1", 10), 10);
        ops.remove("1", new TestObject(1,"user6", 20), 20);
        ops.remove("1", new TestObject(1,"user4", 30), 30);
        ops.remove("1", new TestObject(1,"user3", 40), 40);
        ops.remove("1", new TestObject(1,"user5", 50), 50);
        ops.remove("1", new TestObject(1,"user2", 60), 60);
    }

    @Test
    void test() {
        Set<TestObject> range = ops.range("1", 0, 3);
        Iterator<TestObject> iterator = range.iterator();
        TestObject next = iterator.next();
        TestObject next2 = iterator.next();
        TestObject next3 = iterator.next();
        TestObject next4 = iterator.next();
        boolean hasNext = iterator.hasNext();

        System.out.println("next = " + next);
        System.out.println("next2 = " + next2);
        System.out.println("next3 = " + next3);
        System.out.println("next4 = " + next4);
        System.out.println("hasNext = " + hasNext);

        Assertions.assertThat(next.getName()).isEqualTo("user1");
        Assertions.assertThat(next2.getName()).isEqualTo("user6");
        Assertions.assertThat(next3.getName()).isEqualTo("user4");
        Assertions.assertThat(next4.getName()).isEqualTo("user3");
        Assertions.assertThat(hasNext).isFalse();
    }

    @Test
    void test2() {
        Set<TestObject> reverseRange = ops.reverseRange("1", 0, 3);
        Iterator<TestObject> iterator = reverseRange.iterator();
        TestObject next = iterator.next();
        TestObject next2 = iterator.next();
        TestObject next3 = iterator.next();
        TestObject next4 = iterator.next();
        boolean hasNext = iterator.hasNext();

        System.out.println("next = " + next);
        System.out.println("next2 = " + next2);
        System.out.println("next3 = " + next3);
        System.out.println("next4 = " + next4);
        System.out.println("hasNext = " + hasNext);

        Assertions.assertThat(next.getName()).isEqualTo("user2");
        Assertions.assertThat(next2.getName()).isEqualTo("user5");
        Assertions.assertThat(next3.getName()).isEqualTo("user3");
        Assertions.assertThat(next4.getName()).isEqualTo("user4");
        Assertions.assertThat(hasNext).isFalse();
    }
}
