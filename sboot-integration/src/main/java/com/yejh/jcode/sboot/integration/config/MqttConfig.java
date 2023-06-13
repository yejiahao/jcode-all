package com.yejh.jcode.sboot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-06-16
 * @since x.y.z
 */
@Configuration
@Slf4j
public class MqttConfig {

    @Resource
    private MqttProperties mqttProperties;

    /**
     * 连接器
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // 设置是否清空 session
        mqttConnectOptions.setCleanSession(true);
        // 设置超时时间，默认 30 秒
        mqttConnectOptions.setConnectionTimeout(mqttProperties.getConnectionTimeOut());
        mqttConnectOptions.setKeepAliveInterval(mqttProperties.getKeepAlive());
        mqttConnectOptions.setAutomaticReconnect(true);
        // 设置连接的用户名
        mqttConnectOptions.setUserName(mqttProperties.getUsername());
        // 设置连接的密码
        mqttConnectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        // 服务器地址
        mqttConnectOptions.setServerURIs(new String[]{mqttProperties.getUrl()});
        return mqttConnectOptions;
    }

    /**
     * MQTT 客户端
     */
    @Bean("mqttClientFactory")
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    /**
     * MQTT 生产端发布处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getProducerClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(Integer.parseInt(mqttProperties.getProducerQos()));
        return messageHandler;
    }

    /**
     * MQTT 生产端发布通道
     */
    @Bean("mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT 消费端订阅通道
     */
    @Bean("mqttInboundChannel")
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT 消费端连接配置
     */
    @Bean
    public MessageProducer inbound(@Qualifier("mqttInboundChannel") MessageChannel channel, @Qualifier("mqttClientFactory") MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getConsumerClientId(), factory, mqttProperties.getConsumerTopic());
        adapter.setCompletionTimeout(mqttProperties.getCompletionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(Integer.parseInt(mqttProperties.getConsumerQos()));
        // 设置订阅通道
        adapter.setOutputChannel(channel);
        return adapter;
    }

    /**
     * 消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return message -> {
            Object topic = message.getHeaders().get("mqtt_receivedTopic");
            Object payload = message.getPayload();
            log.info("consumer topic: {}, payload: {}", topic, payload);
        };
    }
}
