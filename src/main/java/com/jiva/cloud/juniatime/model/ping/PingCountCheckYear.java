package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountCheckYear extends PingCount {
  public PingCountCheckYear(Check check) {
    super(check);
  }
  
  public PingCountCheckYear(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountCheckYear(PingCount pingCount) {
    super(pingCount);
  }
}
