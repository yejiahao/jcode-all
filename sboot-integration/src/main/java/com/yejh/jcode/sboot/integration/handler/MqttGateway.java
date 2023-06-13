package com.yejh.jcode.sboot.integration.handler;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-06-16
 * @since x.y.z
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    void send(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
