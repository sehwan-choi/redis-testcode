package com.example.demo.concurrency.annotation;

import com.example.demo.concurrency.annotation.redislock.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StockService {
    private final RedisRepository redisRepository;
    private final StockRepository stockRepository;

    @Transactional
    @RedisLock(key = "redis_lock")
    public Long decrease(String key, Long quantity) {
        Long redisStock = redisRepository.decreaseStock(key, quantity);
        Stock stock = stockRepository.findByName(key).get();
        stock.setQuantity(redisStock);
        return redisStock;
    }

    @Transactional
    public Long decrease2(String key, Long quantity) {
        Stock stock = stockRepository.findByName(key).get();
        stock.decrease(quantity);
        return 0L;
    }
}
