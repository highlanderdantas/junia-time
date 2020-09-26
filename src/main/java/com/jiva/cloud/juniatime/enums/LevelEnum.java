package com.jiva.cloud.juniatime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor @Getter enum LevelEnum {

  LOW("low"), MEDIUM("medium"), HIGH("high"), ULTRA("ultra");

  private String descricao;

}
