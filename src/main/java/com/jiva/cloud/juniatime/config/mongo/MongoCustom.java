package com.jiva.cloud.juniatime.config.mongo;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import com.jiva.cloud.juniatime.utils.ZonedDateTimeReadConverter;
import com.jiva.cloud.juniatime.utils.ZonedDateTimeWriteConverter;

@Configuration
public class MongoCustom {

  @Bean
  public MongoCustomConversions customConversions() {
    var converters = new ArrayList<>();
    converters.add(new ZonedDateTimeReadConverter());
    converters.add(new ZonedDateTimeWriteConverter());
    return new MongoCustomConversions(converters);
  }

}
