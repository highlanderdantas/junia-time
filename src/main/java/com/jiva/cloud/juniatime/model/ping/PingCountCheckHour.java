package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountCheckHour extends PingCount {
  public PingCountCheckHour(Check check) {
    super(check);
  }
  
  public PingCountCheckHour(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountCheckHour(PingCount pingCount) {
    super(pingCount);
  }
  
}
