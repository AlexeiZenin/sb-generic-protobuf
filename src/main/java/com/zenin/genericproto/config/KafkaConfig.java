package com.zenin.genericproto.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Data
@Configuration
@Slf4j
@ConfigurationProperties(prefix = KafkaConfig.PREFIX)
public class KafkaConfig implements InitializingBean {
  public static final String PREFIX = "zenin.kafka";
  @NotBlank private String topicPattern;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info(this.toString());
  }
}
