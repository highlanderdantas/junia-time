package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountCheckDay extends PingCount{
  public PingCountCheckDay(Check check) {
    super(check);
  }
  
  public PingCountCheckDay(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountCheckDay(PingCount pingCount) {
    super(pingCount);
  }
}
