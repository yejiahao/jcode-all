package com.yejh.jcode.nacos;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "boot", autoRefreshed = true)
public class NacosAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosAccessApplication.class, args);
    }

}
