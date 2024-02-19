package com.example.demo.concurrency.annotation;

import com.example.demo.concurrency.annotation.redislock.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private static int count = 1;

    private final RedisRepository redisRepository;
    private final StockRepository stockRepository;

    @RedisLock(key = "redis_lock")
    public Long decrease(String key, Long quantity) {
        log.info("[" + Thread.currentThread().getName() + "]start decrease [" + count++ + "]");
        Long redisStock = redisRepository.decreaseStock(key, quantity);
        Stock stock = stockRepository.findByName(key).get();
        stock.setQuantity(redisStock);
        return redisStock;
    }
}
