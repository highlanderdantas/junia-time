package com.jiva.cloud.juniatime.dto;

import com.jiva.cloud.juniatime.enums.ComponentTypeEnum;
import com.jiva.cloud.juniatime.enums.LevelEnum;
import lombok.Data;

public @Data class ComponentFilter {
	private String name;
	private ComponentTypeEnum type;
	private LevelEnum criticity;
	private LevelEnum impact;
}
