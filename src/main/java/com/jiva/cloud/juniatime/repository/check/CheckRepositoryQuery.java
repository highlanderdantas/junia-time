package com.jiva.cloud.juniatime.repository.check;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jiva.cloud.juniatime.dto.CheckFilter;
import com.jiva.cloud.juniatime.model.Check;

public interface CheckRepositoryQuery {
  
  public Page<Check> filter(CheckFilter checkFilter, Pageable pageable);

}
