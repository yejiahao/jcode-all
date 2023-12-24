package com.yejh.jcode.sboot.restful.scan.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
public class LifetimeBean implements InitializingBean, DisposableBean {

    public void init3() {
        log.info("[init]initMethod 3");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[init]initializingBean 2");
    }

    @PostConstruct
    public void init1() {
        log.info("[init]postConstruct 1");
    }

    public void destroy3() {
        log.info("[destroy]destroyMethod 3");
    }

    @PreDestroy
    public void destroy1() {
        log.info("[destroy]preDestroy 1");
    }

    @Override
    public void destroy() {
        log.info("[destroy]disposableBean 2");
    }
}
