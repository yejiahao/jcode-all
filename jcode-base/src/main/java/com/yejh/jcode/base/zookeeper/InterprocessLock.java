package com.yejh.jcode.base.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * <a href="https://www.runoob.com/w3cnote/zookeeper-locks.html">模拟 50 个线程使用重入排它锁 {@code InterProcessMutex} 同时争抢锁</a>
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-02-08
 * @since 1.0.0
 */
@Slf4j
public class InterprocessLock {

    public static void main(String[] args) throws InterruptedException {
        try (CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkProperties.CONNECT_STRING)
                .sessionTimeoutMs(ZkProperties.SESSION_TIMEOUT)
                .connectionTimeoutMs(ZkProperties.SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1_000, 3, 5_000))
                .build()) {
            curatorFramework.start();

            String lockPath = "/lock";
            InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockPath);
            // 模拟 50 个线程抢锁
            for (int i = 0; i < 50; i++) {
                new Thread(new TestThread(i, lock)).start();
            }
        }
    }

    static class TestThread implements Runnable {
        private int tNum;
        private InterProcessMutex lock;

        public TestThread(int tNum, InterProcessMutex lock) {
            this.tNum = tNum;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                lock.acquire();
                log.info("第 {} 线程获取到了锁", tNum);
                // 等到 1 秒后释放锁
                TimeUnit.SECONDS.sleep(1L);
            } catch (Exception e) {
                log.error("exception: {}", e.getMessage(), e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.error("exception: {}", e.getMessage(), e);
                }
            }
        }
    }

}