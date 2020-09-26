package com.jiva.cloud.juniatime.repository.check;

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
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.service.TagService;

public class CheckRepositoryImpl implements CheckRepositoryQuery {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private TagService tagService;

  @Override
  public Page<Check> filter(CheckFilter checkFilter, Pageable pageable) {
    Criteria[] criteria = filterSearch(checkFilter);
    Query query = new Query(constructCriteria(criteria));
    var count = count(query);
    
    query.with(pageable);
    query.with(Sort.by(Sort.Direction.ASC, "lastChange"));
    List<Check> content = mongoTemplate.find(query, Check.class);
    return new PageImpl<>(content, pageable, count);
  }
  
  private Criteria constructCriteria(Criteria[] criterias) {
    if (criterias.length == 0) return new Criteria();
    else return new Criteria().andOperator(criterias);
  }

  private Criteria[] filterSearch(CheckFilter checkFilter) {
    List<Criteria> criterias = new ArrayList<>();

    if (!StringUtils.isEmpty(checkFilter.getDescricao())) {
      criterias.add(Criteria.where("url").regex(checkFilter.getDescricao().toLowerCase()));
    }
    if (!StringUtils.isEmpty(checkFilter.getStatus())) {
      criterias.add(Criteria.where("status").is(checkFilter.getStatus()));
    }
    if (!StringUtils.isEmpty(checkFilter.getTag())) {
      Tag tag = tagService.findOne(checkFilter.getTag());
      criterias.add(Criteria.where("tags").is(tag));
    }

    return criterias.toArray(new Criteria[criterias.size()]);
  }

  private Long count(Query query) {
    return mongoTemplate.count(query, Check.class);
  }

}
