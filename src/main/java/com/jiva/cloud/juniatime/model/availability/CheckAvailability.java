package com.jiva.cloud.juniatime.model.availability;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jiva.cloud.juniatime.dto.Availability;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"id", "check", "upCount", "downCount", "upComercialCount", "downComercialCount", "year", "month", "day"})
public @Data @NoArgsConstructor abstract class CheckAvailability {
  
  @Id
  private String id;
  private Double value;
  private Double comercialValue;
  private Long upCount;
  private Long downCount;
  private Long upComercialCount;
  private Long downComercialCount;
  private String date;
  private Integer year;
  private Integer month;
  private Integer day;
  private String check;

  public CheckAvailability(Availability availability, String check) {
    this.value = availability.getValue();
    this.comercialValue = availability.getComercialValue();
    this.upCount = availability.getUpCount();
    this.downCount = availability.getDownCount();
    this.upComercialCount = availability.getUpComercialCount();
    this.downComercialCount = availability.getDownComercialCount();
    this.date = availability.getFormattedDate();
    this.year = availability.getDate().getYear();
    this.month = availability.getDate().getMonthValue();
    this.day = availability.getDate().getDayOfMonth();
    this.check = check;
  }
  
}
