package com.jiva.cloud.juniatime.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor class ResponseTimeout {

  private Long codeStatus;
  private Long responseTime;

}
