package com.example.demo.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("crud")
public class CrudObject {

    @Id
    private String id;

    private String firstName;

    private String address;

    public CrudObject() {
    }

    public CrudObject(String id, String firstName, String address) {
        this.id = id;
        this.firstName = firstName;
        this.address = address;
    }

    @Override
    public String toString() {
        return "CrudObject{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
