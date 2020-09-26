package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityMonth;

@Repository
public interface CheckAvailabilityMonthRepository extends MongoRepository<CheckAvailabilityMonth, String> {

  CheckAvailabilityMonth findByCheckAndDate(String check, String date);
  
  List<CheckAvailabilityMonth> findAllByCheck(String check);

  List<CheckAvailabilityMonth> findAllByCheckAndYear(String check, Integer year);
  
  List<CheckAvailabilityMonth> findAllByCheckAndYearAndMonth(String check, Integer year, Integer month);

  List<CheckAvailabilityMonth> findAllByCheckAndDate(String check, String date);

}
