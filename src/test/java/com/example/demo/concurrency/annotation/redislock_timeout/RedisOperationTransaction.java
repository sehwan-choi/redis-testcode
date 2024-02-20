package com.example.demo.concurrency.annotation.redislock_timeout;


import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisOperationTransaction {

    /**
     * 부모트랜잭션의 유무와 관계없이 동시성에 대한 처리는 별도의 트랜잭션으로 동작하기 위함
     *
     * joinPoint가 실제 호출하는 메서드의 @TrasactionalRedisLock에 leaseTime() 시간보다 timeout 시간을 적게 설정해야한다.
     * leaseTime()시간초과가 되면 Excpetion이 발생하지 않고 그대로 로직이 전체 실행되기 때문에 로직안에 JPA를 쓴다면 update, insert, delete가 그대로 반영될 것이다.
     * 그렇기 때문에 leaseTime() 시간초과되기 1초전 쯤에 TransactionTimeOut Exception을 발생시켜 update, insert, delete가 발생하지 않게 한다.
     *  * 일반적으로 leaseTime() 시간초과가 된다면 문제 있는것으로 판단한다.
     *
     * @see StockService#decrease 참조
     *
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 2)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
