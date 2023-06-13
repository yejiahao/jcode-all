package com.yejh.jcode.sboot.restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时器线程配置器
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-05
 * @since 1.0.0
 */
@Configuration
public class ScheduledConfig {

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(2);
    }
}
