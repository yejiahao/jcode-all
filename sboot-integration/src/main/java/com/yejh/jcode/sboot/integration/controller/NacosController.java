package com.yejh.jcode.sboot.integration.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-02-17
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/nacos")
public class NacosController {

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache = false;

    @NacosInjected
    private NamingService namingService;

    @GetMapping("/config/get")
    public Boolean configGet() {
        log.info("useLocalCache: {}", useLocalCache);
        return useLocalCache;
    }

    @GetMapping("/discovery/get")
    public List<Instance> discoveryGet(@RequestParam String serviceName) throws NacosException {
        List<Instance> instances = namingService.getAllInstances(serviceName);
        log.info("instances: {}", instances);
        return instances;
    }

}
