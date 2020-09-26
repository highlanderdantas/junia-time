package com.jiva.cloud.juniatime.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix = "junia")
public class JuniaTimeProperty {

  private String urlAccount;

  private final MongoDBCredentials mongo = new MongoDBCredentials();
  private final Check check = new Check();
  private final Webhook webhook = new Webhook();
  private TypeMessage typeMessage = TypeMessage.LOCAL;

  public @Getter @Setter static class MongoDBCredentials {
    private String host;
    private Integer port;
    private String db;
    private String user;
    private String password;
  }

  public @Getter @Setter static class Check {
    private Long interval;
  }
  
  public enum TypeMessage {
		SLACK, API, LOCAL
  }

  public @Getter @Setter static class Webhook {
    private String url;
    private String channelName;
    private String componentName;

    @Override
    public String toString() {
      String url = new StringBuilder()
          .append(this.url)
          .append("/")
          .append(this.componentName)
          .append("/")
          .append(this.channelName)
        .toString();
      
      System.out.println(url);
      return url;
    }

  }

}
