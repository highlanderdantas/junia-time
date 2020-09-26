package com.jiva.cloud.juniatime.model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jiva.cloud.juniatime.enums.ComponentTypeEnum;
import com.jiva.cloud.juniatime.enums.LevelEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Component {
  
  @Id
  protected String id;
  
  @NotBlank(message = "Campo obrigatório")
  protected String name;
  
  protected ComponentTypeEnum type;
  
  protected LevelEnum criticity;
  
  protected LevelEnum impact;

}
