package com.jiva.cloud.juniatime.model;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
public @Data @NoArgsConstructor class Product {

  @Id
  private String id;

  @NotBlank(message = "Campo obrigatório")
  private String name;

  private String version;

}
