package com.example.demo.concurrency.annotation;

import lombok.Getter;

public class NoStockException extends RuntimeException{

    @Getter
    private final long stock;

    public NoStockException(long stock) {
        this.stock = stock;
    }
}
