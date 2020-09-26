package com.jiva.cloud.juniatime.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.event.CreatedResourceEvent;
import com.jiva.cloud.juniatime.exception.TagEmUsoException;
import com.jiva.cloud.juniatime.exception.TagInexistenteOuInvalidaException;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.availability.TagAvailability;
import com.jiva.cloud.juniatime.repository.CheckRepository;
import com.jiva.cloud.juniatime.repository.TagRepository;
import com.jiva.cloud.juniatime.repository.availability.TagAvailabilityRepository;

/** 
 * @author hgihlander.dantas
 */
@Service
public class TagService {

  @Autowired
  private CheckRepository checkRepository;

  @Autowired
  private ApplicationEventPublisher publisher;
  
  @Autowired
  private TagRepository tagRepository;
  
  @Autowired
  private TagAvailabilityRepository tagAvailabilityRepository;
  

  /**
   * 
   * @param id
   * @return uma tag especifica pelo id
   */
  public Tag findOne(String id) {
    Optional<Tag> savedTag = tagRepository.findById(id);
    if (!savedTag.isPresent()) {
      throw new TagInexistenteOuInvalidaException();
    }
    return savedTag.get();
  }

  /**
   * 
   * @return todas tags
   */
  public List<Tag> findAll() {
    return tagRepository.findAll();
  }

  /**
   * 
   * @param id
   * @param tag
   * @return uma Tag atualizada
   */
  public Tag update(String id, Tag tag) {
    Tag savedTag = this.findOne(id);
    BeanUtils.copyProperties(tag, savedTag, "id");
    return tagRepository.save(savedTag);
  }

  /**
   * 
   * @param id
   * @param description
   * @return uma Tag com a description atualizada
   */
  public Tag updateDescription(String id, String description) {
    Tag savedTag = this.findOne(id);
    savedTag.setDescription(description);
    return tagRepository.save(savedTag);
  }

  /**
   * 
   * @param tag
   * @param response
   * @return uma Tag que foi criada
   */
  public Tag create(Tag tag, HttpServletResponse response) {
    Tag savedTag = tagRepository.save(tag);
    publisher.publishEvent(new CreatedResourceEvent(this, response, savedTag.getId()));
    return savedTag;
  }

  /**
   * @param id
   * 
   * Deleta uma Tag
   */
  public void delete(String id) {
    if (checkRepository.existsByTagsId(findOne(id)))
      throw new TagEmUsoException();
    tagRepository.deleteById(id);

  }

  /**
   * 
   * @param description
   * @return uma tag especifica baseada na description
   */
  public Tag findByRegexDescrption(String description) {
    return tagRepository.findByRegexDescrption(description);
  }

  /**
   * @return retorna um json proprio para dropdowns
   */
  public List<LabelValue> findTagLabel() {
    return findAll().stream()
        .map(tag -> new LabelValue(tag.getId(), StringUtils.capitalize(tag.getDescription())))
        .collect(Collectors.toList());
  }

  /**
   * 
   * @param pageable
   * @return todas as tags paginadas
   */
  public Page<Tag> findAll(Pageable pageable) {
    return tagRepository.findAll(pageable);
  }

  /**
   * @return todas disponibilidades do ano e de um mês especifico, que estão salvar em tabelas
   */
  public List<TagAvailability> findAllTagAvailability() {
    return tagAvailabilityRepository.findAll();
  }

  public TagAvailability findAvailabilityByTag(String id) {
    Optional<Tag> tag = tagRepository.findById(id);
 
    if (tag.isPresent()) return tagAvailabilityRepository.findById(tag.get().getDescription()).get();
    else throw new EmptyResultDataAccessException(1);
  }

}
