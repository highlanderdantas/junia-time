package com.jiva.cloud.juniatime.repository.availability.pingcount;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.ping.PingCount;
import com.jiva.cloud.juniatime.model.ping.PingCountTagDay;

@Repository
public interface PingCountTagDayRepository extends MongoRepository<PingCountTagDay, String> {

  List<PingCount> findAllByCheck(Check check);
  
  List<PingCount> findByTagsIn(List<Tag> tags);
  
  Optional<PingCount> findByCheckId(Check check);

}
