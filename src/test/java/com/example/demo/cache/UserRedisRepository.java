package com.example.demo.cache;

import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<User, Long> {
}
