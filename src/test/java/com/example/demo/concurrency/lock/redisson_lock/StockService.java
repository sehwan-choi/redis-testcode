package com.example.demo.concurrency.lock.redisson_lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockService {
    private final RedisRepository repository;

    public Long decrease(String key, Long quantity) {
        return repository.decreaseStock(key, quantity);
    }
}
