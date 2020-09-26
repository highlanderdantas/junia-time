package com.jiva.cloud.juniatime.repository.client;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import com.jiva.cloud.juniatime.dto.CheckFilter;
import com.jiva.cloud.juniatime.dto.ClientDTO;
import com.jiva.cloud.juniatime.dto.ClientFilter;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.service.CheckService;

public class ClientRepositoryImpl implements ClientRepositoryQuery {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CheckService checkService;

	@Override
	public Page<ClientDTO> filter(ClientFilter clientFilter, Pageable pageable) {
		Criteria[] criteria = filterSearch(clientFilter);
		Query query = new Query(constructCriteria(criteria));
		var count = count(query);
		
		query.with(pageable);
		query.with(Sort.by(Sort.Direction.ASC, "lastChange"));
		List<Client> content = mongoTemplate.find(query, Client.class);
		CheckFilter filter = new CheckFilter();
		List<ClientDTO> response = toCollectionModel(content, filter);

		return new PageImpl<>(response, pageable, count);
	}

    private List<ClientDTO> toCollectionModel(List<Client> content, CheckFilter filter) {
      var response = new ArrayList<ClientDTO>();

      for (Client client : content) {
  			filter.setClientId(client.getId());
  			ClientDTO clientDto = new ClientDTO(client);
  			clientDto.setChecks(checkService.findAllByClientId(filter));
  			response.add(clientDto);
  		}
      
      return response;
    }

	private Long count(Query query) {
		return mongoTemplate.count(query, Client.class);
	}

	private Criteria constructCriteria(Criteria[] criterias) {
		if (criterias.length == 0)
			return new Criteria();
		else
			return new Criteria().andOperator(criterias);
	}

	private Criteria[] filterSearch(ClientFilter clientFilter) {
		List<Criteria> criterias = new ArrayList<>();

		if (!StringUtils.isEmpty(clientFilter.getName())) {
			criterias.add(Criteria.where("name").regex(".*" + clientFilter.getName() + ".*", "i"));
		}
		criterias.add(Criteria.where("enabled").is(true));
		return criterias.toArray(new Criteria[criterias.size()]);
	}

}
