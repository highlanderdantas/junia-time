package com.jiva.cloud.juniatime.repository.component;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jiva.cloud.juniatime.dto.ComponentDTO;
import com.jiva.cloud.juniatime.dto.ComponentFilter;

public interface ComponentRepositoryQuery {
	Page<ComponentDTO> filter(ComponentFilter componentFilter, Pageable pageable);
}
