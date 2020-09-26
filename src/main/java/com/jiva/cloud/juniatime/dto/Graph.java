package com.jiva.cloud.juniatime.dto;

import java.time.LocalDateTime;
import lombok.Data;

public @Data class Graph {
  
  private Integer id;
  private Integer qtd;
  private LocalDateTime data;

}
