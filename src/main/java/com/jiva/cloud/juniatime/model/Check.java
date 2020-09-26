package com.jiva.cloud.juniatime.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.enums.URLTypeEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
public @Data @AllArgsConstructor @NoArgsConstructor class Check {

  @Id
  private String id;

  @JsonIgnoreProperties(value = "products")
  @DBRef
  @Field("client")
  @NotNull
  private Client client;

  @DBRef
  @Field("product")
  @NotNull
  private Product product;

  @DBRef
  @Field("tags")
  @NotNull
  private List<Tag> tags;

  @NotBlank(message = "Campo obrigatório")
  private String url;

  @NotNull(message = "Campo obrigatório")
  @Enumerated(EnumType.STRING)
  private URLTypeEnum type;
  
  @NotNull
  private Boolean savePing;

  // thread safe type
  private AtomicLong errorCount;

  private Long alerTreshold;

  @NotNull
  private StatusEnum status;

  private ZonedDateTime createDate;

  private ZonedDateTime lastChange;

}
