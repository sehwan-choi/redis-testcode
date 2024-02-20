package com.example.demo.concurrency.annotation.redislock;

import com.example.demo.RedisConfig;
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
@SpringBootApplication(scanBasePackages = "com.example.demo.concurrency.annotation.redislock")
@Import(RedisConfig.class)
@Slf4j
public class TestMain {

    private static final String STOCK_KEY = "redisLock_stock";

    @Autowired
    StockService service;

    @Autowired
    RedisRepository repository;

    @Autowired
    StockRepository stockRepository;

    private static int paymentCount = 1;

    @BeforeEach
    void beforeEach() {
        deleteStock(STOCK_KEY);
        paymentCount = 0;
    }

    @Test
    public void 동시에_100개_요청() throws InterruptedException {
        setStock(STOCK_KEY, 10L);

        start(100, STOCK_KEY, 1L);

        StockDto stock = repository.getStock(STOCK_KEY);
        log.info("stock = " + stock + " paymentCount : " + paymentCount);
        Assertions.assertThat(stock.getStock()).isEqualTo(0);
        Assertions.assertThat(paymentCount).isEqualTo(10);
    }

    @Test
    public void 동시에_100개_요청2() throws InterruptedException {
        setStock(STOCK_KEY, 99L);
        start(100, STOCK_KEY, 1L);

        StockDto stock = repository.getStock(STOCK_KEY);
        log.info("stock = " + stock + " paymentCount : " + paymentCount);
        Assertions.assertThat(stock.getStock()).isEqualTo(0);
        Assertions.assertThat(paymentCount).isEqualTo(99);
    }

    @Test
    public void 동시에_10개_요청() throws InterruptedException {
        setStock(STOCK_KEY, 15L);
        start(10, STOCK_KEY, 1L);

        StockDto stock = repository.getStock(STOCK_KEY);
        log.info("stock = " + stock + " paymentCount : " + paymentCount);
        Assertions.assertThat(stock.getStock()).isEqualTo(5);
        Assertions.assertThat(paymentCount).isEqualTo(10);
    }

    void start(int threadCount, String stockKey, long decreaseQuantity) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try {
                    Long decrease = service.decrease(stockKey, decreaseQuantity);
                    log.info("[" + Thread.currentThread().getName() + "]구매 성공! stockQuantity : " + decrease + " paymentCount[" + ++paymentCount + "]");
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
        repository.setStock(key, quantity);
        stockRepository.save(new Stock(1L,key, quantity));
    }

    void deleteStock(String key) {
        repository.deleteStock(key);
        stockRepository.deleteById(1L);
    }
}
