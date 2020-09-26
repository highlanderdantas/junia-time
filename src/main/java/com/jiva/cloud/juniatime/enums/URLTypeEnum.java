package com.jiva.cloud.juniatime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor @Getter enum URLTypeEnum {

  HTTP("http"), HTTPS("https");

  private String descricao;

}
