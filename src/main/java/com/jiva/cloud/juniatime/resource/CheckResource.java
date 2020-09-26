package com.jiva.cloud.juniatime.resource;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.dto.CheckFilter;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.availability.CheckAvailability;
import com.jiva.cloud.juniatime.service.CheckService;

@RestApiController("/checks")
public class CheckResource {

  @Autowired
  private CheckService checkService;

  @GetMapping
  public ResponseEntity<List<Check>> findAll() {
    return ResponseEntity.ok(checkService.findAll());
  }

  @GetMapping("/filter")
  public ResponseEntity<Page<Check>> filter(CheckFilter checkFilter, Pageable pageable) {
    return ResponseEntity.ok(checkService.filter(checkFilter, pageable));
  }

  @PostMapping
  public ResponseEntity<Check> create(@Valid @RequestBody Check check,
      HttpServletResponse response) {
    return ResponseEntity.status(HttpStatus.CREATED).body(checkService.create(check, response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Check> findOne(@PathVariable String id) {
    return ResponseEntity.ok(checkService.findOne(id));
  }

  @GetMapping("/findAllByClientAndProducts")
  public ResponseEntity<List<Check>> findAllByClientAndProducts(
      @Valid @RequestBody CheckFilter checkFilter) {
    return ResponseEntity.ok(checkService.findAllByClientIdAndProductIdIn(checkFilter));
  }
  
  @GetMapping("/findAllByClientId")
  public ResponseEntity<List<Check>> findAllByClientId(CheckFilter checkFilter){
    return ResponseEntity.ok(checkService.findAllByClientId(checkFilter));
  }
  
  @GetMapping("/findByClientIdAnProductId")
  public ResponseEntity<Check> findByClientIdAnProductId(@RequestParam String client,
      @RequestParam String product){
    return ResponseEntity.ok(checkService.findByClientIdAndProduct(client, product));
  }
  
  @GetMapping("/count")
  public ResponseEntity<Long> count() {
    return ResponseEntity.ok(checkService.count());
  }
  
  @GetMapping("/countByStatus")
  public ResponseEntity<Long> countByStatus(@RequestParam StatusEnum status) {
    return ResponseEntity.ok(checkService.countByStatus(status));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Check> update(@PathVariable String id, @Valid @RequestBody Check check) {
    return ResponseEntity.ok(checkService.update(id, check));
  }

  @PutMapping("/incrementErrorCount/{id}")
  public ResponseEntity<Check> incrementErrorCount(@PathVariable String id) {
    return ResponseEntity.ok(checkService.incrementErrorCount(id));
  }

  @GetMapping("/status/{id}")
  public ResponseEntity<Check> updateStatus(@PathVariable(name = "id") String id,
      @RequestParam(name = "status") StatusEnum status) {
    return ResponseEntity.ok(checkService.updateStatus(id, status));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    checkService.delete(id);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/findCheckAvailability/{checkId}")
  public ResponseEntity<CheckAvailability> findCheckAvailabilityByCheck(@PathVariable String checkId) {
    return ResponseEntity.ok(checkService.findAvailabilityByCheck(checkId));
  }
  
}
