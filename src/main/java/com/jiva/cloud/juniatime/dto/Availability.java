package com.jiva.cloud.juniatime.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jiva.cloud.juniatime.enums.TimeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"upCount", "downCount", "upComercialCount", "downComercialCount"})
public @Data @NoArgsConstructor class Availability {

  private Double value;
  private Double comercialValue;
  
  private Long upCount;
  private Long downCount;
  private Long upComercialCount;
  private Long downComercialCount;
  
  private LocalDateTime date;
  private String formattedDate;


  public Availability(Double porcent, Double comercialPercent, Long upCount, Long downCount, Long upComercialCount,
      Long downComercialCount, Integer reference, TimeEnum time) {
    this.value = porcent;
    this.comercialValue = comercialPercent;
    this.upCount = upCount;
    this.downCount = downCount;
    this.upComercialCount = upComercialCount;
    this.downComercialCount = downComercialCount;
    
    
    LocalDateTime dateNow = LocalDateTime.now();
    
    //data salvada para a hora/dia/mes/ano anterior, logo precisa ser subtraido um do parametro atual
    switch(time) {
      case HOUR:
        this.date = dateNow.minusHours(1);
        this.formattedDate = date.format(DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm"));
        break;
      case DAY:
        this.date = dateNow.minusDays(1);
        this.formattedDate = date.format(DateTimeFormatter.ofPattern("dd/M/yyyy"));
        break;
      case MONTH:
        this.date = dateNow.minusMonths(1);
        this.formattedDate = date.format(DateTimeFormatter.ofPattern("M/yyyy"));
        break;
      case YEAR:
        this.date = dateNow.minusYears(1);
        this.formattedDate = String.valueOf(date.getYear());
    }
  }

}
