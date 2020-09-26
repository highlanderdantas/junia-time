package com.jiva.cloud.juniatime.repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.HistoricAlert;


@Repository
public interface HistoricAlertRepository extends MongoRepository<HistoricAlert, String> {
  
  List<HistoricAlert> findAllByDateEventGreaterThan(ZonedDateTime date);
  
  List<HistoricAlert> findAllByClientContainingIgnoreCaseAndDateEventGreaterThan(String client, ZonedDateTime date);
  
}
