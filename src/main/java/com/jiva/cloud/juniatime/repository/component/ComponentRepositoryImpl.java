package com.jiva.cloud.juniatime.repository.component;

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
import com.jiva.cloud.juniatime.dto.ComponentDTO;
import com.jiva.cloud.juniatime.dto.ComponentFilter;
import com.jiva.cloud.juniatime.model.Component;

public class ComponentRepositoryImpl implements ComponentRepositoryQuery {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<ComponentDTO> filter(ComponentFilter componentFilter, Pageable pageable) {
		Criteria[] criteria = filterSearch(componentFilter);
		Query query = new Query(constructCriteria(criteria));
		var count = count(query);
		
		query.with(pageable);
		query.with(Sort.by(Sort.Direction.ASC, "lastChange"));
		List<Component> content = mongoTemplate.find(query, Component.class);
		List<ComponentDTO> response = new ArrayList<>();

		for (Component component : content) {
			ComponentDTO componentDto = new ComponentDTO(component);
			response.add(componentDto);
		}
		return new PageImpl<>(response, pageable, count);
	}

	private Long count(Query query) {
		return mongoTemplate.count(query, Component.class);
	}

	private Criteria constructCriteria(Criteria[] criterias) {
		if (criterias.length == 0)
			return new Criteria();
		else
			return new Criteria().andOperator(criterias);
	}

	private Criteria[] filterSearch(ComponentFilter componentFilter) {
		List<Criteria> criterias = new ArrayList<>();

		if (!StringUtils.isEmpty(componentFilter.getName())) {
			criterias.add(Criteria.where("name").regex(".*" + componentFilter.getName() + ".*", "i"));
		}
		
		if (!StringUtils.isEmpty(componentFilter.getType())) {
          criterias.add(Criteria.where("type").regex(".*" + componentFilter.getType() + ".*", "i"));
        }
		
		if (!StringUtils.isEmpty(componentFilter.getCriticity())) {
		  criterias.add(Criteria.where("criticity").regex(".*" + componentFilter.getCriticity() + ".*", "i"));
		}
		
		return criterias.toArray(new Criteria[criterias.size()]);
	}
}
