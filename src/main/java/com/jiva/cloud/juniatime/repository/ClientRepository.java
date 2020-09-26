package com.jiva.cloud.juniatime.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.repository.client.ClientRepositoryQuery;

@Repository
public interface ClientRepository extends MongoRepository<Client, String>, ClientRepositoryQuery {

  long countByEnabledTrue();

  List<Client> findAllByEnabledTrue();
  
  Optional<Client> findById(String id);

}
