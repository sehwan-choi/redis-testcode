package com.example.demo.concurrency.lock.lettuce_lock;

import com.example.demo.RedisConfig;
import com.example.demo.concurrency.annotation.redislock.NoStockException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.example.demo.concurrency.lock.lettuce_lock")
@Import(RedisConfig.class)
@Slf4j
public class TestMain {

    private static final String STOCK_KEY = "lettuce_stock";

    @Autowired
    RedisRepository redisRepository;

    @Autowired
    LettuceStockServiceFacade facade;

    @BeforeEach
    void beforeEach() {
        deleteStock(STOCK_KEY);
        setStock(STOCK_KEY, 10L);
    }

    @Test
    public void 동시에_100개_요청() throws InterruptedException {
        start(100, STOCK_KEY, 1L);

        StockDto stock = redisRepository.getStock(STOCK_KEY);
        Assertions.assertThat(stock.getStock()).isEqualTo(0);
        System.out.println("stock = " + stock);
    }

    void start(int threadCount, String stockKey, long decreaseQuantity) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try {
                    Long decrease = facade.decrease(stockKey, decreaseQuantity);
                    log.info("[" + Thread.currentThread().getName() + "]구매 성공! stockQuantity : " + decrease);
                } catch (NoStockException e2) {
                    log.info("[" + Thread.currentThread().getName() + "]구매 실패! stock : " + e2.getStock());
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
        redisRepository.setStock(key, quantity);
    }

    void deleteStock(String key) {
        redisRepository.deleteStock(key);
    }
}
