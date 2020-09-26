package com.jiva.cloud.juniatime.model;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
public @Data @NoArgsConstructor class Tag {
  
  @Id
  private String id;

  @NotBlank
  private String description;
}
