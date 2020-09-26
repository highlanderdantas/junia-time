package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityHour;

@Repository
public interface CheckAvailabilityHourRepository extends MongoRepository<CheckAvailabilityHour, String> {

  CheckAvailabilityHour findByCheckAndDate(String check, String date);

  List<CheckAvailabilityHour> findAllByCheck(String check);

  List<CheckAvailabilityHour> findAllByCheckAndYearAndMonthAndDay(String check, Integer year, Integer month, Integer day);

}
