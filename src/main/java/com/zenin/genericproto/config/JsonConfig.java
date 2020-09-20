package com.zenin.genericproto.config;

import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

  @Bean
  GsonBuilder gsonBuilder() {
    return new GsonBuilder();
  }
}
