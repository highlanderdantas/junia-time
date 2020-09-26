package com.jiva.cloud.juniatime.repository.availability;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.CheckAvailability;

@Repository
public interface CheckAvailabilityRepository extends MongoRepository<CheckAvailability, String> {

}
