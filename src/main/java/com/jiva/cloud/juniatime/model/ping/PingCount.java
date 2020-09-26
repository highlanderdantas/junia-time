package com.jiva.cloud.juniatime.model.ping;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.Data;

/**
 * @author Daniel Marques
 * classe sera utilizada para incrementar qtd de pings up/down de cada check,
 *  desfazendo a necessidade de armazenar todos os pings.
 */
public @Data abstract class PingCount {
  
  @Id
  private String id;
  
  @DBRef
  @Field("check")
  private Check check;
  
  @DBRef
  @Field("tags")
  private List<Tag> tags;
  
  private String name;
  
  private Long upCount;
  private Long upComercialCount;
  
  private Long downCount;
  private Long downComercialCount;
  
  private ZonedDateTime lastChange;
  
  /**
   * incrementa quantidade de pings up para determinado check
   */
  public void incrementUpCount() {
    this.upCount++;
  }
  
  /**
   * incrementa quantidade de pings down para determinado check
   */
  public void incrementDownCount() {
    this.downCount++;
  }
  
  public void incrementUpComercialCount() {
    this.upComercialCount++;
  }
  
  public void incrementDownComercialCount() {
    this.downComercialCount++;
  }

  public void incrementUpCount(Long quantity) {
    this.upCount+=quantity;
  }
  
  public void incrementDownCount(Long quantity) {
    this.downCount+=quantity;
  }
  
  public void incrementUpComercialCount(Long quantity) {
    this.upComercialCount+=quantity;
  }
  
  public void incrementDownComercialCount(Long quantity) {
    this.downComercialCount+=quantity;
  }
  
  public void setCheck(Check check) {
    this.check = check;
  }
  
  public PingCount(Check check) {
    this.check = check;
    this.tags = check.getTags();
    this.upCount = this.downCount = 0l;
    this.upComercialCount = this.downComercialCount = 0l;
    this.lastChange = ZonedDateTime.now();
  }
  
  public PingCount(List<Tag> tags) {
    this.tags = tags;
    this.upCount = this.downCount = 0l;
    this.upComercialCount = this.downComercialCount = 0l;
    this.lastChange = ZonedDateTime.now();
  }
  
  public PingCount(PingCount pingCount) {
    this.id = pingCount.getId();
    this.name = pingCount.getName();
    this.check = pingCount.getCheck();
    this.tags = pingCount.getCheck().getTags();
    this.upCount = pingCount.getUpCount();
    this.upComercialCount = pingCount.getUpComercialCount();
    this.downComercialCount = pingCount.getDownComercialCount();
    this.downCount = pingCount.getDownCount();
    this.lastChange = ZonedDateTime.now();
  }
  
  public PingCount() {
    this.lastChange = ZonedDateTime.now();
  }
  
}
