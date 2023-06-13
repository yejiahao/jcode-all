package com.yejh.jcode.sboot.restful.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Service
@Slf4j
@Transactional
public class CircuitBreakerAspect {

    // 阀值
    private static final Integer THRESHOLD = 3;
    //记录失败的次数
    private Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();
    // 记录被保护的次数
    private Map<String, AtomicInteger> breakCounter = new ConcurrentHashMap<>();

    @Around("execution(* com.yejh.jcode.sboot.restful.controller.*.*(..))")
    public Object doWithCircuitBreaker(ProceedingJoinPoint pjp) throws Throwable {
        // 获取当前执行的方法
        String signature = pjp.getSignature().toLongString();
        log.info("Invoke: {}", signature);
        Object retVal;
        try {
            if (counter.containsKey(signature)) {
                // 失败次数达到预制，如果保护次数没到，返回 null
                if (counter.get(signature).get() > THRESHOLD && breakCounter.get(signature).get() < THRESHOLD) {
                    log.warn("Circuit breaker return null, break: {} times.", breakCounter.get(signature).incrementAndGet());
                    return null;
                }
            } else {
                counter.put(signature, new AtomicInteger(0));
                breakCounter.put(signature, new AtomicInteger(0));
            }
            retVal = pjp.proceed();
            counter.get(signature).set(0);
            breakCounter.get(signature).set(0);
        } catch (Throwable t) {
            log.warn("Circuit breaker counter: {}, Throwable {}", counter.get(signature).incrementAndGet(), t.getMessage());
            breakCounter.get(signature).set(0);
            throw t;
        }
        return retVal;
    }
}
