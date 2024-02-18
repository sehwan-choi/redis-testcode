package com.example.demo.concurrency.annotation;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Slf4j
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    private long quantity;

    @Version
    private int version;

    public Stock(String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        log.info("[" + Thread.currentThread().getName() + "]Stock decrease start ====> " + this.quantity);
        if ((this.quantity - quantity) < 0) {
            throw new NoStockException(this.quantity);
        }

        this.quantity -= quantity;
        log.info("[" + Thread.currentThread().getName() + "]Stock decrease end ====> " + this.quantity);
    }
}
