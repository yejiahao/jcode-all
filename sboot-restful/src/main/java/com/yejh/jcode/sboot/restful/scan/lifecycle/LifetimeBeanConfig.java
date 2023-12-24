package com.yejh.jcode.sboot.restful.scan.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifetimeBeanConfig {

    @Bean(initMethod = "init3", destroyMethod = "destroy3")
    public LifetimeBean getLifetimeBean() {
        return new LifetimeBean();
    }

}
