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
import org.springframework.web.bind.annotation.ResponseStatus;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.availability.TagAvailability;
import com.jiva.cloud.juniatime.service.TagService;

@RestApiController("/tags")
public class TagResource {

  @Autowired
  private TagService tagService;

  @GetMapping
  public ResponseEntity<List<Tag>> findAll() {
    return ResponseEntity.ok(tagService.findAll());
  }
  
  @GetMapping("/page")
  public ResponseEntity<Page<Tag>> findAll(Pageable pageable) {
    return ResponseEntity.ok(tagService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Tag> findOne(@PathVariable String id) {
    return ResponseEntity.ok(tagService.findOne(id));
  }
  
  @GetMapping("/labelValue")
  public ResponseEntity<List<LabelValue>> findTagLabel() {
    return ResponseEntity.ok(tagService.findTagLabel());
  }
  
  @GetMapping("/calculeAvailability")
  public ResponseEntity<List<TagAvailability>> findTagAndAvailability() {
    return ResponseEntity.ok(tagService.findAllTagAvailability());
  }
  
  @GetMapping("/findAvailability")
  public ResponseEntity<TagAvailability> findAvailabilityByTag(@RequestParam(required = true) String tag) {
    return ResponseEntity.ok(tagService.findAvailabilityByTag(tag));
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Tag> update(@PathVariable String id, @Valid @RequestBody Tag tag) {
    return ResponseEntity.ok(tagService.update(id, tag));
  }

  @PutMapping("/{id}/description")
  public ResponseEntity<Tag> updateDescription(@PathVariable("id") String id,
      @RequestBody String description) {
    return ResponseEntity.status(HttpStatus.OK).body(tagService.updateDescription(id, description));
  }

  @PostMapping
  public ResponseEntity<Tag> save(@Valid @RequestBody Tag tag, HttpServletResponse response) {
    return new ResponseEntity<Tag>(tagService.create(tag, response), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable String id) {
    tagService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
