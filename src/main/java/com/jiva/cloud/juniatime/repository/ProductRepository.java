package com.jiva.cloud.juniatime.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.jiva.cloud.juniatime.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
  
  Optional<Product> findById(String id);

}
