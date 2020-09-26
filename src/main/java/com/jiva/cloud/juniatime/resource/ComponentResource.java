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
import com.jiva.cloud.juniatime.dto.ComponentDTO;
import com.jiva.cloud.juniatime.dto.ComponentFilter;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.model.Component;
import com.jiva.cloud.juniatime.service.ComponentService;

@RestApiController("/components")
public class ComponentResource {

  @Autowired
  private ComponentService componentService;

  @GetMapping
  public ResponseEntity<Page<ComponentDTO>> filter(ComponentFilter componentFilter,
      Pageable pageable) {
    return ResponseEntity.ok(componentService.filter(componentFilter, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Component> findOne(@PathVariable String id) {
    return ResponseEntity.ok(componentService.findOne(id));
  }

  @PostMapping
  public ResponseEntity<Component> save(@Valid @RequestBody Component component) {
    return ResponseEntity.status(HttpStatus.CREATED).body(componentService.save(component));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Component> update(@PathVariable String id,
      @Valid @RequestBody Client client) {
    return ResponseEntity.status(HttpStatus.OK).body(componentService.update(id, client));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    componentService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/typeLabelValue")
  public ResponseEntity<List<LabelValue>> findComponentTypeLabel() {
    return ResponseEntity.ok(componentService.findComponentTypeLabel());
  }

  @GetMapping("/criticityLabelValue")
  public ResponseEntity<List<LabelValue>> findComponentCriticityLabel() {
    return ResponseEntity.ok(componentService.findComponentCriticityLabel());
  }

}
