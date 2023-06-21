package com.adoptpet.server.commons.config.kafka;

import com.adoptpet.server.adopt.dto.aggregation.AggregationDto;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.commons.properties.KafkaAdoptProperties;
import com.adoptpet.server.commons.properties.KafkaAggregationProperties;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class ListenerConfiguration {

    private final KafkaAdoptProperties adoptProperties;
    private final KafkaAggregationProperties aggregationProperties;

    // KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaAdoptContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaAdoptConsumer());
        return factory;
    }

    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, Message> kafkaAdoptConsumer() {
        JsonDeserializer<Message> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");

        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, adoptProperties.getBroker())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, adoptProperties.getGroupId())
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AggregationDto> kafkaAggregationContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AggregationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaAggregationConsumer());
        return factory;
    }

    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, AggregationDto> kafkaAggregationConsumer() {
        JsonDeserializer<AggregationDto> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");

        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, aggregationProperties.getBroker())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, aggregationProperties.getGroupId())
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

}
