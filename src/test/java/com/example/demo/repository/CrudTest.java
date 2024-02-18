package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.example.demo.repository")
@EnableRedisRepositories
public class CrudTest {

    @Autowired
    CrudObjectRepository repository;


    @Test
    void test() {
        CrudObject crudObject = new CrudObject("1", "choi","a");
        repository.save(crudObject);

        CrudObject crudObject2 = new CrudObject("2", "kim", "b");
        repository.save(crudObject2);

        CrudObject crudObject3 = new CrudObject("3", "lee","c");
        repository.save(crudObject3);

        CrudObject crudObject4 = new CrudObject("4", "part",  "d");
        repository.save(crudObject4);

        CrudObject crudObject5 = new CrudObject("5", "ma",  "e");
        repository.save(crudObject5);
    }


    @Test
    void test2() {
        CrudObject crudObject = new CrudObject("6", "kang","f");
        repository.save(crudObject);
    }

    @Test
    void test3() {
        CrudObject crudObject = repository.findById("1").get();
        System.out.println("crudObject = " + crudObject);
    }
}
