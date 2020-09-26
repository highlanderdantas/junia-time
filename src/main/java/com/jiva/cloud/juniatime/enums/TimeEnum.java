package com.jiva.cloud.juniatime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor @Getter enum TimeEnum {
  HOUR("hour"), DAY("day"), MONTH("month"), YEAR("year");
  
  private String descricao;
}
