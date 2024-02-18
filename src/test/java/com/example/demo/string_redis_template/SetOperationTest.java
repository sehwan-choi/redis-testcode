package com.example.demo.string_redis_template;

import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;

import java.util.Set;

@SpringBootTest(classes = MainTest.class)
public class SetOperationTest {
    //     특정 오퍼레이션 직접 주입
    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> ops;



//    @Test
//    void init() {
//        ops.add("AA", "a", "b", "c");
//        ops.add("BB","qqq", "zzz");
//    }



    @BeforeEach
    void beforeEach() {
        ops.add("AA", "a", "b", "c");
        ops.add("BB","qqq", "zzz");
    }

    @AfterEach
    void afterEach() {
        ops.remove("AA","a","b","c");
        ops.remove("BB", "qqq","zzz", "AAAAAAAAAAAA");
    }

    @Test
    void test() {
        Set<String> set = ops.members("AA");
        set.forEach(f -> System.out.println("[AA]f = " + f));

        Set<String> set2 = ops.members("BB");
        set2.forEach(f -> System.out.println("[BB]f = " + f));
    }

    @Test
    void test2() {
        String a = ops.pop("AA");
        System.out.println("a = " + a);
        String b = ops.pop("AA");
        System.out.println("b = " + b);
        String c = ops.pop("AA");
        System.out.println("c = " + c);
        String d = ops.pop("AA");
        System.out.println("d = " + d);

        Assertions.assertThat(a).isNotNull();
        Assertions.assertThat(b).isNotNull();
        Assertions.assertThat(c).isNotNull();
        Assertions.assertThat(d).isNull();
    }

    @Test
    void test3() {
        String a = ops.pop("BB");
        System.out.println("a = " + a);
        String b = ops.pop("BB");
        System.out.println("b = " + b);
        String c = ops.pop("BB");
        System.out.println("c = " + c);

        Assertions.assertThat(a).isNotNull();
        Assertions.assertThat(b).isNotNull();
        Assertions.assertThat(c).isNull();
    }

    @Test
    void test4() {
        Long remove = ops.remove("BB", "qqq");
        System.out.println("remove = " + remove);
        Boolean member = ops.isMember("BB", "qqq");

        Long b = ops.add("BB", "AAAAAAAAAAAA");
        System.out.println("b = " + b);
        Boolean member2 = ops.isMember("BB", "AAAAAAAAAAAA");

        Assertions.assertThat(member).isFalse();
        Assertions.assertThat(member2).isTrue();
    }
}
