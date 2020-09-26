package com.jiva.cloud.juniatime.repository.availability.pingcount;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.ping.PingCount;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckHour;

@Repository
public interface PingCountCheckHourRepository extends MongoRepository<PingCountCheckHour, String>{

  List<PingCount> findAllByCheck(String check);
  
  Optional<PingCount> findByCheckId(Check check);

}
