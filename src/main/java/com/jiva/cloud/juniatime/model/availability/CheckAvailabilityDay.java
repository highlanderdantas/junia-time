package com.jiva.cloud.juniatime.model.availability;

import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.dto.Availability;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class CheckAvailabilityDay extends CheckAvailability{
  public CheckAvailabilityDay(Availability availability, String check) {
    super(availability, check);
  }
}