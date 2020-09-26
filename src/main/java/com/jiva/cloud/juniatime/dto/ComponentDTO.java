package com.jiva.cloud.juniatime.dto;

import com.jiva.cloud.juniatime.model.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @Getter @Setter @NoArgsConstructor class ComponentDTO extends Component {

  public ComponentDTO(Component component) {
    this.id = component.getId();
    this.name = component.getName();
    this.type = component.getType();
    this.criticity = component.getCriticity();
    this.impact = component.getImpact();
  }
}
