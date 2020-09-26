package com.jiva.cloud.juniatime.model.availability;

import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.dto.Availability;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class TagAvailabilityYear extends TagAvailability{
  public TagAvailabilityYear(Availability availability, String tag) {
    super(availability, tag);
  }
}