package com.yejh.jcode.sboot.integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-06-16
 * @since x.y.z
 */
@Component
@ConfigurationProperties("mqtt.config")
@Data
public class MqttProperties {

    private String url;

    private String username;

    private String password;

    private Integer keepAlive;

    private Integer connectionTimeOut;

    private String producerClientId;

    private String producerQos;

    private String producerTopic;

    private String consumerClientId;

    private String consumerQos;

    private String consumerTopic;

    private Integer completionTimeout;
}

