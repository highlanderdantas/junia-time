package com.jiva.cloud.juniatime.repository.availability;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityYear;

@Repository
public interface CheckAvailabilityYearRepository extends MongoRepository<CheckAvailabilityYear, String> {

    CheckAvailabilityYear findByCheckAndDate(String check, String date);

    List<CheckAvailabilityYear> findAllByCheck(String check);

}
