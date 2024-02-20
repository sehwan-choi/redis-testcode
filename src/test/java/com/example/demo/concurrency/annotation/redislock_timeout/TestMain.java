package com.example.demo.concurrency.annotation.redislock_timeout;

import com.example.demo.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.*;

@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.example.demo.concurrency.annotation.redislock_timeout")
@Import(RedisConfig.class)
@Slf4j
public class TestMain {

    private static final String STOCK_KEY = "redisLock_timeout_stock";

    @Autowired
    StockService service;

    @Autowired
    RedisRepository repository;

    @Autowired
    StockRepository stockRepository;

    private static int paymentSuccessCount = 1;

    private static int timeoutCount = 0;

    private static int paymentFailCount = 0;

    @BeforeEach
    void beforeEach() {
        deleteStock(STOCK_KEY);
        paymentSuccessCount = 0;
        timeoutCount = 0;
    }

    /**
     * redis 상에서 stock = 0이 되고
     * DB 상에서 stock = 10이 된다.
     * @throws InterruptedException
     */
    @Test
    @DisplayName("@Transactional Timeout 시간이 @TransactionalRedisLock leaseTime보다 작은경우, JpaSystemException(Transaction TimeOut Excpetion) 발생하여 Transaction 정상 롤백")
    public void 동시에_15개_요청() throws InterruptedException {
        long quantity = 10L;
        int threadCount = 15;
        setStock(STOCK_KEY, quantity);

        start(threadCount, () -> {
            try {
                return service.decrease(STOCK_KEY, 1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        StockDto stock = repository.getStock(STOCK_KEY);
        log.info("stock = " + stock + " paymentCount : " + paymentSuccessCount);

        Stock findStock = stockRepository.findById(1L).get();
        System.out.println("findStock = " + findStock);

        Assertions.assertThat(timeoutCount).isEqualTo(quantity);
        Assertions.assertThat(paymentSuccessCount).isEqualTo(0);
        Assertions.assertThat(findStock.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("@Transactional Timeout 시간이 @TransactionalRedisLock leaseTime보다 큰경우, leaseTime 타임아웃 되어도 Transaction 롤백되지 않고 정삭 로직 수행")
    public void 동시에_15개_요청2() throws InterruptedException {
        long quantity = 10;
        int threadCount = 15;
        setStock(STOCK_KEY, quantity);

        start(threadCount, () -> {
            try {
                return service.decrease2(STOCK_KEY, 1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        StockDto stock = repository.getStock(STOCK_KEY);
        log.info("stock = " + stock + " paymentCount : " + paymentSuccessCount);

        Stock findStock = stockRepository.findById(1L).get();
        System.out.println("findStock = " + findStock);

        Assertions.assertThat(timeoutCount).isEqualTo(0);
        Assertions.assertThat(findStock.getQuantity()).isLessThan(quantity);
        Assertions.assertThat(paymentSuccessCount).isGreaterThanOrEqualTo((int)quantity);
    }

    void start(int threadCount, Supplier<Long> supplier) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try {
                    Long decrease = supplier.get();
                    paymentSuccessCount++;
                    log.info("[" + Thread.currentThread().getName() + "]구매 성공! stockQuantity : " + decrease + " paymentCount[" + paymentSuccessCount + "]");
                } catch (NoStockException e2) {
                    paymentFailCount++;
                    log.info("[" + Thread.currentThread().getName() + "]구매 실패! stock : " + e2.getStock() + " paymentFailCount[" + paymentFailCount + "]");
                } catch (JpaSystemException e3) {
                    timeoutCount++;
                    log.info("timeoutCount = " + timeoutCount);
                    log.info("JpaSystemException = " + e3);
                } catch (Exception e) {
                    log.info("e = " + e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    void setStock(String key, Long quantity) {
        repository.setStock(key, quantity);
        stockRepository.save(new Stock(1L,key, quantity));
    }

    void deleteStock(String key) {
        repository.deleteStock(key);
        stockRepository.deleteById(1L);
    }
}
