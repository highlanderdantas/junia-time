package com.jiva.cloud.juniatime;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableAsync
@EnableScheduling
public class JuniaTimeApplication {

  public static void main(String[] args) {
    // setando timezone para utc, afim de padronizar a hora
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(JuniaTimeApplication.class);
  }
}
