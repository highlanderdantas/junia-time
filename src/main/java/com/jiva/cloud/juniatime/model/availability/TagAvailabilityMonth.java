package com.jiva.cloud.juniatime.model.availability;

import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.dto.Availability;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class TagAvailabilityMonth extends TagAvailability{
  public TagAvailabilityMonth(Availability availability, String tag) {
    super(availability, tag);
  }
}