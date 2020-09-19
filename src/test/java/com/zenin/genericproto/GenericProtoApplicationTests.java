package com.zenin.genericproto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Slf4j
class GenericProtoApplicationTests {

    @Container
    public KafkaContainer kafka = new KafkaContainer();

    @Test
    void contextLoads() {
    }

}
