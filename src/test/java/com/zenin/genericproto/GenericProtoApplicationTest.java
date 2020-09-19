package com.zenin.genericproto;

import com.zenin.genericproto.test.KafkaContainerSBAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Slf4j
class GenericProtoApplicationTest {
    public static final int NUM_PARTITIONS = 2;
    @Container
    public static KafkaContainer kafka = new KafkaContainerSBAware();

    @Configuration
    @Import(GenericProtoApplication.class)
    public static class Beans {
        @Bean
        NewTopic testTopic() {
            return new NewTopic("prod.orders", NUM_PARTITIONS, (short) 1);
        }
    }

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Test
    void contextLoads() {
        registry.getListenerContainers().forEach(container ->
                ContainerTestUtils.waitForAssignment(container, NUM_PARTITIONS)
        );
    }
}
