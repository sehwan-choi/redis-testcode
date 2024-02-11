package com.example.demo.redis_template;

import com.example.demo.TestObject;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;

@SpringBootTest
@DisplayName("RedisTemplate-HashOperation: ")
public class HashOperationTest {

    @Resource(name = "redisTemplate")
    HashOperations<String, String, TestObject> ops;

//    @Test
//    void init() {
//        ops.put("aa", "aa", new TestObject(1, "AAAAAAA"));
//        ops.put("aa", "bb", new TestObject(1, "BBBBBBB"));
//        ops.put("aa", "cc", new TestObject(1, "CCCCCCC"));
//    }


    /**
     * Redis에 key : "aa", hashKey : "aa", "bb","cc" 저장된 데이터 모두 삭제
     */
//    @Test
//    void remove() {
//        ops.delete("aa", "aa", "bb", "cc");
//    }

    @BeforeEach
    void beforeEach() {
        ops.put("aa", "aa", new TestObject(1, "AAAAAAA"));
        ops.put("aa", "bb", new TestObject(2, "BBBBBBB"));
        ops.put("aa", "cc", new TestObject(3, "CCCCCCC"));
    }

    @AfterEach
    void afterEach() {
        ops.delete("aa", "aa", "bb", "cc");
    }

    /**
     * Redis에 들어있는 데이터 전부 출력
     */
    @Test
    @DisplayName("Redis 데이터 전부 출력후 비교")
    void test() {
        TestObject testObject = ops.get("aa", "aa");
        System.out.println("aa = " + testObject);

        TestObject testObject2 = ops.get("aa", "bb");
        System.out.println("bb = " + testObject2);

        TestObject testObject3 = ops.get("aa", "cc");
        System.out.println("cc = " + testObject3);

        Assertions.assertThat(testObject).isEqualTo(new TestObject(1, "AAAAAAA"));
        Assertions.assertThat(testObject2).isEqualTo(new TestObject(2, "BBBBBBB"));
        Assertions.assertThat(testObject3).isEqualTo(new TestObject(3, "CCCCCCC"));
    }

    /**
     * Redis에 key: "aa" , hashKey: "aa" 가져와서 출력후
     * 이름 변경하여 저장(정상 업데이트됌)
     */
    @Test
    @DisplayName("Redis에 key: aa , hashKey: aa 가져와서 이름 변경하여 put")
    void test2() {
        TestObject testObject = ops.get("aa", "aa");
        System.out.println("testObject = " + testObject);

        testObject.setName("UPDATE!!");

        ops.put("aa", "aa", testObject);
        TestObject updateObject = ops.get("aa", "aa");
        System.out.println("updateObject = " + updateObject);

        Assertions.assertThat(updateObject.getName()).isEqualTo(testObject.getName());
    }

    /**
     * Redis에 key: "aa" , hashKey: "aa" 가져와서 출력후
     * 삭제 후 같은 hashKey로 저장(정상 삭제후 저장됌)
     */
    @Test
    @DisplayName("Redis에 key: aa , hashKey: aa 가져와서 삭제후 같은 HashKey로 put")
    void test3() {
        TestObject testObject = ops.get("aa", "aa");
        System.out.println("testObject = " + testObject);

        ops.delete("aa","aa");
        TestObject removeObject = ops.get("aa", "aa");
        System.out.println("removeObject = " + removeObject);

        ops.put("aa", "aa", new TestObject(1, "DELETE AND INSERT!!"));
        TestObject updateObject = ops.get("aa", "aa");
        System.out.println("updateObject = " + updateObject);

        Assertions.assertThat(testObject).isEqualTo(new TestObject(1, "AAAAAAA"));
        Assertions.assertThat(removeObject).isNull();
        Assertions.assertThat(updateObject).isEqualTo(new TestObject(1, "DELETE AND INSERT!!"));
    }

    @Test
    @DisplayName("Redis에 key, HashKey로 저장유무 확인")
    void test4() {
        Boolean isTrue = ops.hasKey("aa", "aa");
        Assertions.assertThat(isTrue).isTrue();
    }
}
