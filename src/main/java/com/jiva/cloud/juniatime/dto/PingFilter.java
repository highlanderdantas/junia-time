package com.jiva.cloud.juniatime.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import com.jiva.cloud.juniatime.enums.TimeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor class PingFilter {

  
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
  private LocalDateTime dateFrom;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTo;

  private Integer hourFrom;
  private Integer hourTo;
  
  private String check;

  @NotNull
  private TimeEnum time;
  private String tag;


  public PingFilter(String tag, TimeEnum time) {
    this.tag = tag;
    this.time = time;
	}

}
