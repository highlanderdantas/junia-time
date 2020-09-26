package com.jiva.cloud.juniatime.repository.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jiva.cloud.juniatime.dto.ClientDTO;
import com.jiva.cloud.juniatime.dto.ClientFilter;

public interface ClientRepositoryQuery {
	public Page<ClientDTO> filter(ClientFilter clientFilter, Pageable pageable);
}
