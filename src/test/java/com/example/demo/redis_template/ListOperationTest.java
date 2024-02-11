package com.example.demo.redis_template;

import com.example.demo.TestObject;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;

@SpringBootTest
@DisplayName("RedisTemplate-ListOperation: ")
public class ListOperationTest {

    @Resource(name = "redisTemplate")
    ListOperations<String, TestObject> ops;

//    @Test
//    void init() {
//        ops.rightPush("aaa", new TestObject(1, "AAAAAAA"));
//        ops.rightPush("aaa", new TestObject(1, "BBBBBBB"));
//        ops.rightPush("aaa", new TestObject(1, "CCCCCCC"));
//        ops.leftPush("aaa", new TestObject(1, "1111111"));
//    }
//
//    @Test
//    void remove() {
//        ops.remove("aaa", 1, new TestObject(1, "AAAAAAA"));
//        ops.remove("aaa", 1, new TestObject(1, "BBBBBBB"));
//        ops.remove("aaa", 1, new TestObject(1, "CCCCCCC"));
//        ops.remove("aaa", 1, new TestObject(1, "1111111"));
//    }

    @BeforeEach
    void beforeEach() {
        ops.rightPush("aaa", new TestObject(2, "AAAAAAA"));
        ops.rightPush("aaa", new TestObject(3, "BBBBBBB"));
        ops.rightPush("aaa", new TestObject(4, "CCCCCCC"));
        ops.leftPush("aaa", new TestObject(1, "1111111"));
    }

    @AfterEach
    void afterEach() {
        ops.remove("aaa", 1, new TestObject(2, "AAAAAAA"));
        ops.remove("aaa", 1, new TestObject(3, "BBBBBBB"));
        ops.remove("aaa", 1, new TestObject(4, "CCCCCCC"));
        ops.remove("aaa", 1, new TestObject(1, "1111111"));
    }

    @Test
    @DisplayName("Redis 데이터 전부 출력후 비교")
    void test() {
        TestObject first = ops.index("aaa", 0);
        System.out.println("first = " + first);

        TestObject second = ops.index("aaa", 1);
        System.out.println("second = " + second);

        TestObject third = ops.index("aaa", 2);
        System.out.println("third = " + third);

        TestObject four = ops.index("aaa", 3);
        System.out.println("four = " + four);

        TestObject five = ops.index("aaa", 4);
        System.out.println("five = " + five);

        Assertions.assertThat(first).isEqualTo(new TestObject(1, "1111111"));
        Assertions.assertThat(second).isEqualTo(new TestObject(2, "AAAAAAA"));
        Assertions.assertThat(third).isEqualTo(new TestObject(3, "BBBBBBB"));
        Assertions.assertThat(four).isEqualTo(new TestObject(4, "CCCCCCC"));
        Assertions.assertThat(five).isNull();
    }

    @Test
    @DisplayName("Redis에 저장된 List에서 왼쪽부터 가져와서 비교")
    void test2() {
        TestObject _111 = ops.leftPop("aaa");
        TestObject _AAA = ops.leftPop("aaa");
        TestObject _BBB = ops.leftPop("aaa");
        TestObject _CCC = ops.leftPop("aaa");
        TestObject _Null = ops.leftPop("aaa");

        Assertions.assertThat(_111).isEqualTo(new TestObject(1, "1111111"));
        Assertions.assertThat(_AAA).isEqualTo(new TestObject(2, "AAAAAAA"));
        Assertions.assertThat(_BBB).isEqualTo(new TestObject(3, "BBBBBBB"));
        Assertions.assertThat(_CCC).isEqualTo(new TestObject(4, "CCCCCCC"));
        Assertions.assertThat(_Null).isNull();
    }

    @Test
    @DisplayName("Redis에 저장된 List에서 오른쪽부터 가져와서 비교")
    void test3() {
        TestObject _CCC = ops.rightPop("aaa");
        TestObject _BBB = ops.rightPop("aaa");
        TestObject _AAA = ops.rightPop("aaa");
        TestObject _111 = ops.rightPop("aaa");
        TestObject _Null = ops.rightPop("aaa");

        Assertions.assertThat(_CCC).isEqualTo(new TestObject(4, "CCCCCCC"));
        Assertions.assertThat(_BBB).isEqualTo(new TestObject(3, "BBBBBBB"));
        Assertions.assertThat(_AAA).isEqualTo(new TestObject(2, "AAAAAAA"));
        Assertions.assertThat(_111).isEqualTo(new TestObject(1, "1111111"));
        Assertions.assertThat(_Null).isNull();
    }
}
