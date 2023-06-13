package com.yejh.jcode.base.thread;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂类（用于设置线程名称）
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-06
 * @since 1.0.0
 */
public class MyThreadFactory implements ThreadFactory {

    private String poolName;

    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public MyThreadFactory(String poolName) {
        SecurityManager s = System.getSecurityManager();
        group = Objects.nonNull(s) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, poolName + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
