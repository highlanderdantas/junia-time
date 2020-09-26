package com.jiva.cloud.juniatime.dto;

import java.util.List;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.model.Product;
import lombok.Data;

public @Data class CheckFilter {

  private String descricao;
  private StatusEnum status;
  private String tag;
  private List<Product> products;
  private String clientId;

}
