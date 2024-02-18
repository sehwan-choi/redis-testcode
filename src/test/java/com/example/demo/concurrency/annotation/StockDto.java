package com.example.demo.concurrency.annotation;


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
        log.info("[" + Thread.currentThread().getName() + "]StockDto decrease start ====> " + this.stock);
        if ((this.stock - quantity) < 0) {
            log.info("[" + Thread.currentThread().getName() + "]NoStockException! = " + this.stock);
            throw new NoStockException(this.stock);
        }
        this.stock -= quantity;
        log.info("[" + Thread.currentThread().getName() + "]StockDto decrease end ====> " + this.stock);
    }
}
