package com.example.demo.concurrency.lock.lettuce_lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceStockServiceFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public Long decrease(String key, Long quantity) throws InterruptedException {

        while(!redisLockRepository.lock(key)){ //계속해서 lock 획득 시도
            Thread.sleep(100); //Spinlock 방식이 redis에게 주는 부하를 줄여주기 위해 sleep
        }

        try{
            return stockService.decrease(key,quantity);
        } finally{
            redisLockRepository.unlock(key);
        }

    }
}
