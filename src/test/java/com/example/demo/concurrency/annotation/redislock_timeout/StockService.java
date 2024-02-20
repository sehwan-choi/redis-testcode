package com.example.demo.concurrency.annotation.redislock_timeout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private static int count = 1;

    private final RedisRepository redisRepository;
    private final StockRepository stockRepository;

    /**
     * waitTime = 20000L : 20초 동안 락을 획득하려고 시도한다.
     * leaseTime = 1500L : lock 획득에 성공한 이후, lease time(5초) 이 지나면 자동으로 lock을 해제한다. (단, lock이 해제 될때 Exception은 발생되지 않는다. 정상흐름대로 로직실행된다.)
     * TimeUnit.MILLISECONDS : 시간기준은 밀리세컨트 기준(waitTime = 20초, leaseTime = 1.5초)
     *
     * @see RedisOperationTransaction#proceed
     */
    @TransactionalRedisLock(key = "redis_lock", waitTime = 20000L, leaseTime = 1500L, timeUnit = TimeUnit.MILLISECONDS)
    public Long decrease(String key, Long quantity) throws InterruptedException {
        log.info("[" + Thread.currentThread().getName() + "]start decrease [" + count++ + "]");
        Long redisStock = redisRepository.decreaseStock(key, quantity);
        Stock stock = stockRepository.findByName(key).get();
        stock.setQuantity(redisStock);
        Thread.sleep(1000);
        return redisStock;
    }

    /**
     * waitTime = 20000L : 20초 동안 락을 획득하려고 시도한다.
     * leaseTime = 500L : lock 획득에 성공한 이후, lease time(5초) 이 지나면 자동으로 lock을 해제한다. (단, lock이 해제 될때 Exception은 발생되지 않는다. 정상흐름대로 로직실행된다.)
     * TimeUnit.MILLISECONDS : 시간기준은 밀리세컨트 기준(waitTime = 20초, leaseTime = 0.5초)
     *
     * @see RedisOperationTransaction#proceed @Transactional에 설정된 timeout이 leaseTime보다 작은경우에는 leaseTime이 초과했을때 Excpetion이 발생하지 않으므로 정상적으로 DB 반영이 되어 원하지않은 결과가 나올수 있다.
     * (*) leaseTime 초과가 되면 기본적으로 문제가 있다고 판단한다.
     */
    @TransactionalRedisLock(key = "redis_lock", waitTime = 20000L, leaseTime = 10L, timeUnit = TimeUnit.MILLISECONDS)
    public Long decrease2(String key, Long quantity) throws InterruptedException {
        log.info("[" + Thread.currentThread().getName() + "]start decrease [" + count++ + "]");
        Long redisStock = redisRepository.decreaseStock(key, quantity);
        log.info("[" + Thread.currentThread().getName() + "]redis stock count decrease! count : " + redisStock);
        Stock stock = stockRepository.findByName(key).get();
        stock.setQuantity(redisStock);
        log.info("[" + Thread.currentThread().getName() + "]end decrease db stockCount[" + stock.getQuantity() + "]");
        return redisStock;
    }
}
