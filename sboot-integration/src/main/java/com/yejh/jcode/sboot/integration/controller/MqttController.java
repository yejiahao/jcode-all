package com.yejh.jcode.sboot.integration.controller;

import com.yejh.jcode.sboot.integration.config.MqttProperties;
import com.yejh.jcode.sboot.integration.handler.MqttGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-06-16
 * @since x.y.z
 */
@Slf4j
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Resource
    private MqttProperties mqttProperties;

    @Resource
    private MqttGateway mqttGateway;

    @PostMapping("/send")
    public boolean send() {
        mqttGateway.send(mqttProperties.getProducerTopic(), "yejh, hello world");
        return true;
    }

}
