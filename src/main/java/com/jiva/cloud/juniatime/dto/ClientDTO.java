package com.jiva.cloud.juniatime.dto;

import java.util.List;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @Getter @Setter @NoArgsConstructor class ClientDTO extends Client {

  List<Check> checks;

  public ClientDTO(Client client) {
    this.id = client.getId();
    this.name = client.getName();
    this.products = client.getProducts();
    this.createDate = client.getCreateDate();
    this.lastChange = client.getLastChange();
    this.enabled = client.getEnabled();
  }
}
