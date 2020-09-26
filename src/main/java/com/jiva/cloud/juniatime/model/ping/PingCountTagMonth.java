package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountTagMonth extends PingCount {
  public PingCountTagMonth(Check check) {
    super(check);
  }
  
  public PingCountTagMonth(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountTagMonth(PingCount pingCount) {
    super(pingCount);
  }
}
