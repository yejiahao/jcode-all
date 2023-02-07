package com.yejh.jcode.base.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="https://www.runoob.com/w3cnote/zookeeper-java-setup.html">原生 API</a>
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-02-07
 * @since 1.0.0
 */
@Slf4j
public class ZkConnDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(ZkProperties.CONNECT_STRING, ZkProperties.SESSION_TIMEOUT, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                cdl.countDown();
            }
        });
        cdl.await();
        log.info("state: {}", zk.getState());
    }
}
