package com.jiva.cloud.juniatime.resource;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.dto.ClientDTO;
import com.jiva.cloud.juniatime.dto.ClientFilter;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.service.ClientService;

@RestApiController("/clients")
public class ClientResource {

  @Autowired
  private ClientService clientService;

  @GetMapping
  public ResponseEntity<List<Client>> findAll() {
    return ResponseEntity.ok(clientService.findAll());
  }
  
  @GetMapping("/filter")
  public ResponseEntity<Page<ClientDTO>> filter(ClientFilter clientFilter, Pageable pageable) {
    return ResponseEntity.ok(clientService.filter(clientFilter, pageable));
  }
  
  @GetMapping("/labelValue")
  public ResponseEntity<List<LabelValue>> findClientLabel() {
    return ResponseEntity.ok(clientService.findClientLabel());
  }
  
  @GetMapping("/count")
  public ResponseEntity<Long> findCountAll(){
    return ResponseEntity.ok(clientService.count());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Client> findOne(@PathVariable String id) {
    return ResponseEntity.ok(clientService.findOne(id));
  }

  @PostMapping
  public ResponseEntity<Client> save(@Valid @RequestBody Client client) {
    return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Client> update(@PathVariable String id,
      @Valid @RequestBody Client client) {
    return ResponseEntity.status(HttpStatus.OK).body(clientService.update(id, client));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    clientService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
