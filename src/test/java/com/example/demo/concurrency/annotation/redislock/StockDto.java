package com.example.demo.concurrency.annotation.redislock;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class StockDto implements Serializable {

    private long stock;

    public void decrease(Long quantity) {
        if ((this.stock - quantity) < 0) {
            throw new NoStockException(this.stock);
        }
        this.stock -= quantity;
        log.info("[" + Thread.currentThread().getName() + "]StockDto decrease success ====> [" + (this.stock+1) + "] -> [" + this.stock + "]");
    }
}
