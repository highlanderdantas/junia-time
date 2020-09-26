package com.jiva.cloud.juniatime.model.ping;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import lombok.NoArgsConstructor;

@Document
public @NoArgsConstructor class PingCountTagYear extends PingCount {
  public PingCountTagYear(Check check) {
    super(check);
  }
  
  public PingCountTagYear(List<Tag> tags) {
    super(tags);
  }
  
  public PingCountTagYear(PingCount pingCount) {
    super(pingCount);
  }
}
