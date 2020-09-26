package com.jiva.cloud.juniatime.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
public @Data @NoArgsConstructor @AllArgsConstructor class Client {

  @Id
  protected String id;

  @NotBlank(message = "Campo obrigatório")
  protected String name;

  @DBRef
  @Field("products")
  protected List<Product> products;

  protected LocalDateTime createDate;

  protected LocalDateTime lastChange;

  @NotNull
  protected Boolean enabled;

}
