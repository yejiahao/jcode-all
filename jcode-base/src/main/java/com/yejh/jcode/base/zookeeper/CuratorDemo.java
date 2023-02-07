package com.yejh.jcode.base.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * <a href="https://www.bbsmax.com/A/qVdewllMJP/">{@code CuratorFramework} 增删查改</a>
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-02-07
 * @since 1.0.0
 */
@Slf4j
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        try (CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkProperties.CONNECT_STRING)
                .sessionTimeoutMs(ZkProperties.SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1_000, 3))
                .namespace("")
                .build()) {

            log.info("启动");
            curatorFramework.start();

            log.info("创建节点");
            curatorFramework.create().forPath("/user", "Ye".getBytes());

            log.info("查询节点");
            byte[] result = curatorFramework.getData().forPath("/user");
            log.info("{}", new String(result));

            log.info("更新节点");
            curatorFramework.setData().forPath("/user", "Jiahao".getBytes());
            result = curatorFramework.getData().forPath("/user");
            log.info("{}", new String(result));

            log.info("删除节点");
            curatorFramework.delete().forPath("/user");
            log.info("isExists: {}", Objects.nonNull(curatorFramework.checkExists().forPath("/user")));

            log.info("关闭");
        }
    }
}
