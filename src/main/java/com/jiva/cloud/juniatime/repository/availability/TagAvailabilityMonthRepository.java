package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jiva.cloud.juniatime.model.availability.TagAvailabilityMonth;

@Repository
public interface TagAvailabilityMonthRepository extends MongoRepository<TagAvailabilityMonth, String> {

    TagAvailabilityMonth findByTagAndDate(String tag, String date);

	List<TagAvailabilityMonth> findAllByTag(String tag);
	
	List<TagAvailabilityMonth> findAllByTagAndYear(String tag, Integer year);

}
