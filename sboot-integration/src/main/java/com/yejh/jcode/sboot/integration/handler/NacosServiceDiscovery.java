package com.yejh.jcode.sboot.integration.handler;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.HashMap;
import java.util.Map;

public class NacosServiceDiscovery {

    public static void main(String[] args) throws NacosException {
        NamingService naming = NamingFactory.createNamingService(NacosCommonProps.serverAddress);
        naming.registerInstance("nacos.test.2", "11.11.11.11", 8888, "TEST1");

        Instance instance = new Instance();
        instance.setIp("55.55.55.55");
        instance.setPort(9999);
        instance.setHealthy(false);
        instance.setWeight(2.0);
        Map<String, String> instanceMeta = new HashMap<>();
        instanceMeta.put("site", "et2");
        instance.setMetadata(instanceMeta);

        naming.registerInstance("nacos.test.3", instance);
        naming.registerInstance("nacos.test.4", instance);
    }
}
