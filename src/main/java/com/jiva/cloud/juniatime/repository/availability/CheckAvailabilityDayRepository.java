package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityDay;

@Repository
public interface CheckAvailabilityDayRepository extends MongoRepository<CheckAvailabilityDay, String> {

  CheckAvailabilityDay findByCheckAndDate(String check, String date);

  List<CheckAvailabilityDay> findAllByCheck(String check);
  
  List<CheckAvailabilityDay> findAllByCheckAndYearAndMonth(String check, Integer year, Integer month);

}
