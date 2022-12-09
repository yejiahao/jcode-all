package com.yejh.jcode.base.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.Objects;
import java.util.Properties;

/**
 * kafka 生产者
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2019-09-21
 * @since 2.3.1
 */
public class KProducerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_CONNECT);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < 10; i++) {
                String val = "message" + i;
                producer.send(new ProducerRecord<>(KafkaProperties.TOPIC, Objects.toString(i), val), (metadata, exception) ->
                        System.out.printf("onCompletion offset: %d%n", metadata.offset())
                );
                System.out.printf("sent: %s%n", val);
            }
        }
    }
}
