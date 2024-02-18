package com.example.demo.concurrency.lock.lettuce_lock;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockDto implements Serializable {

    private long stock = 0;

    public void decrease(Long quantity) {
        if ((this.stock - quantity) < 0) {
            throw new NoStockException(this.stock);
        }
        this.stock -= quantity;
    }
}
