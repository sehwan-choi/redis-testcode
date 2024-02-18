package com.example.demo.concurrency.lock.lettuce_lock;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRepository {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, StockDto> ops;

    public StockDto getStock(String key) {
        return ops.get(key);
    }

    public Long decreaseStock(String key , Long quantity) {
        StockDto stock = getStock(key);
        stock.decrease(quantity);
        ops.set(key, stock);
        return stock.getStock();
    }

    public void setStock(String key, Long quantity) {
        ops.set(key, new StockDto(quantity));
    }

    public void deleteStock(String key) {
        ops.getAndDelete(key);
    }
}
