package com.yejh.jcode.sboot.integration.handler;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.yejh.jcode.sboot.integration.handler.NacosCommonProps.*;

@Slf4j
public class NacosConfigManagement {

    public static void main(String[] args) throws NacosException, InterruptedException {
        Properties properties = new Properties();
        properties.put("serverAddr", NacosCommonProps.serverAddress);
        ConfigService configService = NacosFactory.createConfigService(properties);

        String content = configService.getConfig(DATA_ID_1, GROUP_1, 5_000L);
        log.info("content: {}", content);

        configService.publishConfig(DATA_ID_2, GROUP_2, "content2");
        configService.removeConfig(DATA_ID_1, GROUP_1);

        Listener listener = new Listener() {

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("receive: {}", configInfo);
            }
        };
        configService.addListener(DATA_ID_1, GROUP_1, listener);
        configService.removeListener(DATA_ID_1, GROUP_1, listener);

        while (true) {
            TimeUnit.SECONDS.sleep(5L);
        }

    }
}
