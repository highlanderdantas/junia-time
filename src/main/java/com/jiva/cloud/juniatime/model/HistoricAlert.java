package com.jiva.cloud.juniatime.model;

import java.time.ZonedDateTime;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.event.StatusCheckEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
public @Data @NoArgsConstructor class HistoricAlert {
  

  @Id
  private String id;

  private String client;
  private String product;
  
  @NotNull
  private StatusEnum status;
  
  private ZonedDateTime dateEvent;
  
  public HistoricAlert(StatusCheckEvent event) {
    Check check = event.getCheck();
    this.client = check.getClient().getName();
    this.product = check.getProduct().getName();
    this.status = event.getStatus();
    this.dateEvent = ZonedDateTime.now();
  }
  
}

