package com.yejh.jcode.base;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * {@link Delayed#getDelay(TimeUnit)} <= 0 时出队列，{@link Delayed#compareTo(Object)} 堆排序
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-12-24
 * @since 2.0.0
 */
@Slf4j
public class DelayQueueCase {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<User> dq = new DelayQueue<>();
        dq.offer(new User(325, "wangwu", 5 * 3000L));
        dq.offer(new User(123, "zhangsan", 3 * 3000L));
        dq.offer(new User(222, "chener", 2 * 3000L));
        dq.offer(new User(424, "lisi", 4 * 3000L));
        log.info("dq: {}", dq);
        while (!dq.isEmpty()) {
            User user = dq.take();
            log.info("user: {}", user);
        }
    }
}

@Data
@ToString
class User implements Delayed {

    private int age;
    private String name;
    private long delayTime;

    public User(int age, String name, long delayTime) {
        this.age = age;
        this.name = name;
        this.delayTime = delayTime + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Integer.compare(this.getAge(), ((User) o).getAge());
    }
}
