package com.zenin.genericproto.test;

import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

@TestConfiguration
@Slf4j
public class MockRegistryBeans {
  private final KafkaAutoConfiguration kafkaAutoConfiguration;

  public MockRegistryBeans(KafkaAutoConfiguration kafkaAutoConfiguration) {
    this.kafkaAutoConfiguration = kafkaAutoConfiguration;
  }

  @Bean
  SchemaRegistryClient mockRegistry() {
    return new MockSchemaRegistryClient();
  }

  @Bean
  ConsumerFactory<?, ?> mockedConsumerFactory(
      ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
    final DefaultKafkaConsumerFactory<String, DynamicMessage> consumerFactory =
        (DefaultKafkaConsumerFactory<String, DynamicMessage>)
            kafkaAutoConfiguration.kafkaConsumerFactory(customizers);
    consumerFactory.setValueDeserializer(new KafkaProtobufDeserializer<>(mockRegistry()));
    consumerFactory.setKeyDeserializer(new StringDeserializer());
    return consumerFactory;
  }

  @Bean
  ProducerFactory<?, ?> mockedProducerFactory(
      ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
    final DefaultKafkaProducerFactory<String, Message> producerFactory =
        (DefaultKafkaProducerFactory<String, Message>)
            kafkaAutoConfiguration.kafkaProducerFactory(customizers);
    producerFactory.setValueSerializer(new KafkaProtobufSerializer<>(mockRegistry()));
    producerFactory.setKeySerializer(new StringSerializer());
    return producerFactory;
  }
}
