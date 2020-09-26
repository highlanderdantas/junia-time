package com.jiva.cloud.juniatime.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @Getter @AllArgsConstructor enum StatusEnum {

  UP("up"), DOWN("down"), PAUSED("paused"),RESTARTED("restarted");

  String descricao;
}
