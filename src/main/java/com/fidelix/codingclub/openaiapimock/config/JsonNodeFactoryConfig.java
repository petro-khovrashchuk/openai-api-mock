package com.fidelix.codingclub.openaiapimock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.node.JsonNodeFactory;

@Configuration
public class JsonNodeFactoryConfig {

  @Bean
  public JsonNodeFactory jsonNodeFactory() {
    return JsonNodeFactory.instance;
  }

}
