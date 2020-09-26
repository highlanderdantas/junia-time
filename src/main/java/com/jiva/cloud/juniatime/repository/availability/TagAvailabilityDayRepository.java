package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.TagAvailabilityDay;

@Repository
public interface TagAvailabilityDayRepository extends MongoRepository<TagAvailabilityDay, String> {

  TagAvailabilityDay findByTagAndDate(String tag, String date);
  
  List<TagAvailabilityDay> findAllByTagAndYearAndMonth(String tag, Integer year, Integer month);

  List<TagAvailabilityDay> findAllByTag(String tag);

}
