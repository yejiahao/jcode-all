package com.yejh.jcode.sboot.integration;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "boot", autoRefreshed = true)
public class SbootIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbootIntegrationApplication.class, args);
    }

}
