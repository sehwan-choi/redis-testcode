package com.example.demo.concurrency.lock.lettuce_lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LettuceStockServiceFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    private static int count = 1;

    public Long decrease(String key, Long quantity) throws InterruptedException {

        while(!redisLockRepository.lock(key)){ //계속해서 lock 획득 시도
            Thread.sleep(100); //Spinlock 방식이 redis에게 주는 부하를 줄여주기 위해 sleep
        }

        try{
            log.info("[" + Thread.currentThread().getName() + "]start decrease [" + count++ + "]");
            return stockService.decrease(key,quantity);
        } finally{
            redisLockRepository.unlock(key);
        }

    }
}
