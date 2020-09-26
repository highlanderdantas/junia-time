package com.jiva.cloud.juniatime.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.Tag;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

  Optional<Tag> findById(String id);
  
  @Query("{ 'description' : { $regex: ?0 } }")
  Tag findByRegexDescrption(String regexp);

}
