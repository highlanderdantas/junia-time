package com.jiva.cloud.juniatime.config.web;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  public static final String PREFIX = "/api";
  
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
      configurer.addPathPrefix(PREFIX, c -> c.isAnnotationPresent(RestApiController.class));
  }
  
  @Bean
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(200);
    executor.setMaxPoolSize(250);
    executor.setQueueCapacity(200);
    executor.initialize();
    return executor;
  }

}
