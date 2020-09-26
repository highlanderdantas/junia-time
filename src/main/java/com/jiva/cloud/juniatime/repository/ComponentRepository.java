package com.jiva.cloud.juniatime.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.Component;
import com.jiva.cloud.juniatime.repository.component.ComponentRepositoryQuery;

@Repository
public interface ComponentRepository extends MongoRepository<Component, String>, ComponentRepositoryQuery {
  
  Component findByName(String name);
  
}
