package com.jiva.cloud.juniatime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor @Getter enum ComponentTypeEnum {

  APPLICATION("application"), CONTAINER("container"), NODE("node"), CLOUD("cloud");

  private String descricao;

}
