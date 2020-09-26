package com.jiva.cloud.juniatime.resource;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.model.Product;
import com.jiva.cloud.juniatime.service.ProductService;

@RestApiController("/products")
public class ProductResource {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    return ResponseEntity.ok(productService.findAll());
  }
  
  @GetMapping("/labelValue")
  public ResponseEntity<List<LabelValue>> findProductLabel() {
    return ResponseEntity.ok(productService.findProductLabel());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> findOne(@PathVariable String id) {
    return ResponseEntity.ok(productService.findOne(id));
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product product,
      HttpServletResponse response) {
    return new ResponseEntity<Product>(productService.create(product, response),
        HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable String id,
      @Valid @RequestBody Product product) {
    return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
