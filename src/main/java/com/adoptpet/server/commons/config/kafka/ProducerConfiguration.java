package com.adoptpet.server.commons.config.kafka;

import com.adoptpet.server.adopt.dto.aggregation.AggregationDto;
import com.adoptpet.server.adopt.dto.chat.Message;
import com.adoptpet.server.commons.properties.KafkaAdoptProperties;
import com.adoptpet.server.commons.properties.KafkaAggregationProperties;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class ProducerConfiguration {

    private final KafkaAdoptProperties adoptProperties;
    private final KafkaAggregationProperties aggregationProperties;

    // Kafka ProducerFactory를 생성하는 Bean 메서드
    @Bean
    public ProducerFactory<String, Message> adoptProducerFactory() {
        return new DefaultKafkaProducerFactory<>(adoptProducerConfiguration());
    }

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> adoptProducerConfiguration() {
        return ImmutableMap.<String, Object>builder()
                .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, adoptProperties.getBroker())
                .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .build();
    }

    // KafkaTemplate을 생성하는 Bean 메서드
    @Bean
    public KafkaTemplate<String, Message> adoptKafkaTemplate() {
        return new KafkaTemplate<>(adoptProducerFactory());
    }

    @Bean
    public ProducerFactory<String, AggregationDto> aggregationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(adoptProducerConfiguration());
    }

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> aggregationProducerConfiguration() {
        return ImmutableMap.<String, Object>builder()
                .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, aggregationProperties.getBroker())
                .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .build();
    }

    // KafkaTemplate을 생성하는 Bean 메서드
    @Bean
    public KafkaTemplate<String, AggregationDto> aggregationKafkaTemplate() {
        return new KafkaTemplate<>(aggregationProducerFactory());
    }


}
