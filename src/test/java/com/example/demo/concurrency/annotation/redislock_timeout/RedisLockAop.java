package com.example.demo.concurrency.annotation.redislock_timeout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAop {

    private final RedissonClient redissonClient;
    private final RedisOperationTransaction redissonCallTransaction;

    @Around("@annotation(com.example.demo.concurrency.annotation.redislock_timeout.TransactionalRedisLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TransactionalRedisLock redisLock = method.getAnnotation(TransactionalRedisLock.class);

        /* create key */
//        String key = this.createKey(signature.getParameterNames(), joinPoint.getArgs(), redisLock.key());
        String key = redisLock.key();

        /* get rLock 객체 */
        RLock rock = redissonClient.getLock(key);

        try {
            /* get lock */
            boolean isPossible = rock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), redisLock.timeUnit());
            if (!isPossible) {
                log.info("[" + Thread.currentThread().getName() + "]isPossible : " + key);
                return false;
            }

            log.info("[" + Thread.currentThread().getName() + "]RedisLock Key : " + key);

            /* service call */
            return redissonCallTransaction.proceed(joinPoint);
        } catch (Exception e){
            log.info("AOP Excpetion = " + e);
            throw e;
        } finally {
            Exception throable = null;
            try {
                rock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("IllegalMonitorStateException = " + e);
            }
        }
    }

    /**
     * Redisson Key Create
     * @param parameterNames
     * @param args
     * @param key
     * @return
     */
    private String createKey(String[] parameterNames, Object[] args, String key) {
        String resultKey = key;

        /* key = parameterName */
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(key)) {
                resultKey += args[i];
                break;
            }
        }

        return resultKey;
    }
}
