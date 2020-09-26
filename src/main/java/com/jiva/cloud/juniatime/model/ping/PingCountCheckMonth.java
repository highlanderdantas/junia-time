package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountCheckMonth extends PingCount {
  public PingCountCheckMonth(Check check) {
    super(check);
  }
  
  public PingCountCheckMonth(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountCheckMonth(PingCount pingCount) {
    super(pingCount);
  }
 
}
