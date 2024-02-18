package com.example.demo.concurrency.lock.redisson_lock;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonStockServiceFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public Long decrease(String key, Long quantity) throws InterruptedException {
        RLock lock = redissonClient.getLock("redissonLock");

        try{
            boolean available = lock.tryLock(5,1, TimeUnit.SECONDS); // lock 획득
            if(!available){
                System.out.println("lock 획득 실패");
                return -1L;
            }
            return stockService.decrease(key,quantity); //서비스 로직 실

        } catch(InterruptedException e){
            throw new RuntimeException(e);

        } finally{
            lock.unlock(); //lock 해제
        }
    }
}
