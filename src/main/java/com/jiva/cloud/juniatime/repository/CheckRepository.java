package com.jiva.cloud.juniatime.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.model.Product;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.repository.check.CheckRepositoryQuery;


@Repository
public interface CheckRepository extends MongoRepository<Check, String>, CheckRepositoryQuery {

  Boolean existsByTagsId(Tag tag);

  Boolean existsByClientId(Client client);

  List<Check> findAllByTagsId(Tag tag);

  long countByStatus(StatusEnum status);

  long count();
  
  List<Check> findAllByClientIdAndProductIdIn(Client client, List<Product> products);
  
  Optional<Check >findByClientIdAndProductId(Client client, Product product);
  
  List<Check> findAllByClientId(Client client);
  
  Optional<Check> findById(String id);

}
