package com.example.demo.redis_template;

import com.example.demo.TestObject;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@SpringBootTest
@DisplayName("RedisTemplate-SetOperation: ")
public class SetOperationTest {

    //     특정 오퍼레이션 직접 주입
    @Resource(name = "redisTemplate")
    private SetOperations<String, TestObject> ops;



//    @Test
//    void init() {
//        ops.add("A", new TestObject(1, "a"), new TestObject(2, "b"), new TestObject(3, "c"));
//        ops.add("B", new TestObject(990, "qqq"), new TestObject(991, "zzz"));
//    }


    @BeforeEach
    void beforeEach() {
        ops.add("A", new TestObject(1, "a"), new TestObject(2, "b"), new TestObject(3, "c"));
        ops.add("B", new TestObject(990, "qqq"), new TestObject(991, "zzz"));
    }

    @AfterEach
    void afterEach() {
        ops.remove("A", new TestObject(1, "a"));
        ops.remove("A", new TestObject(2, "b"));
        ops.remove("A", new TestObject(3, "c"));
        ops.remove("B", new TestObject(990, "qqq"));
        ops.remove("B", new TestObject(991, "zzz"));
        ops.remove("B", new TestObject(990, "AAAAAAAAAAAA"));
    }

    @Test
    void test() {
        Set<TestObject> set = ops.members("A");
        set.forEach(f -> System.out.println("[A]f = " + f));

        Set<TestObject> set2 = ops.members("B");
        set2.forEach(f -> System.out.println("[B]f = " + f));
    }

    @Test
    void test2() {
        TestObject a = ops.pop("A");
        System.out.println("a = " + a);
        TestObject b = ops.pop("A");
        System.out.println("b = " + b);
        TestObject c = ops.pop("A");
        System.out.println("c = " + c);
        TestObject d = ops.pop("A");
        System.out.println("d = " + d);

        Assertions.assertThat(a).isNotNull();
        Assertions.assertThat(b).isNotNull();
        Assertions.assertThat(c).isNotNull();
        Assertions.assertThat(d).isNull();
    }

    @Test
    void test3() {
        TestObject a = ops.pop("B");
        System.out.println("a = " + a);
        TestObject b = ops.pop("B");
        System.out.println("b = " + b);
        TestObject c = ops.pop("B");
        System.out.println("c = " + c);

        Assertions.assertThat(a).isNotNull();
        Assertions.assertThat(b).isNotNull();
        Assertions.assertThat(c).isNull();
    }

    @Test
    void test4() {
        Long remove = ops.remove("B", new TestObject(990, "qqq"));
        System.out.println("remove = " + remove);
        Boolean member = ops.isMember("B", new TestObject(990, "qqq"));

        TestObject object = new TestObject(990, "AAAAAAAAAAAA");
        Long b = ops.add("B", object);
        System.out.println("b = " + b);
        Boolean member2 = ops.isMember("B", new TestObject(990, "AAAAAAAAAAAA"));

        Assertions.assertThat(member).isFalse();
        Assertions.assertThat(member2).isTrue();
    }
}
