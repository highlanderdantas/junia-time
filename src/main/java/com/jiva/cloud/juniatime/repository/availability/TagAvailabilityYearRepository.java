package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jiva.cloud.juniatime.model.availability.TagAvailabilityYear;

@Repository
public interface TagAvailabilityYearRepository extends MongoRepository<TagAvailabilityYear, String> {

    TagAvailabilityYear findByTagAndDate(String tag, String date);

    List<TagAvailabilityYear> findAllByTag(String tag);

    List<TagAvailabilityYear> findAllByTagAndYear(String tag, Integer year);

}
