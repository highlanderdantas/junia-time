package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountTagDay extends PingCount {
  public PingCountTagDay(Check check) {
    super(check);
  }
  
  public PingCountTagDay(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountTagDay(PingCount pingCount) {
    super(pingCount);
  }
 
}
