package com.jiva.cloud.juniatime.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.jiva.cloud.juniatime.dto.ComponentDTO;
import com.jiva.cloud.juniatime.dto.ComponentFilter;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.exception.ComponentInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.model.Component;
import com.jiva.cloud.juniatime.repository.ComponentRepository;


@Service
public class ComponentService {

  @Autowired
  private ComponentRepository componentRepository;


  /**
   * 
   * @return lista de Clientes
   */
  public List<Component> findAll() {
    return componentRepository.findAll();
  }

  /**
   * 
   * @param clientFilter parametros de busca
   * @param pageable
   * @return lista de clientes paginada
   */
  public Page<ComponentDTO> filter(ComponentFilter componentFilter, Pageable pageable) {
    return componentRepository.filter(componentFilter, pageable);
  }

  /**
   * 
   * @param id de um cliente
   * @return um cliente
   */
  public Component findOne(String id) {
    Optional<Component> component = componentRepository.findById(id);
    if (!component.isPresent())
      throw new ComponentInexistenteOuInvalidoException();

    return component.get();
  }

  /**
   * 
   * @param id
   * @param client
   * @return um cliente
   */
  public Component update(String id, Client client) {
    Component savedComponent = findOne(id);
    BeanUtils.copyProperties(client, savedComponent, "id");
    return componentRepository.save(savedComponent);
  }

  /**
   * 
   * @param id
   * 
   *        Desativa um cliente
   */
  public void delete(String id) {
    Component componentSave = this.findOne(id);
    this.componentRepository.delete(componentSave);
  }

  /**
   * 
   * @param client
   * @return um cliente salvo
   */
  public Component save(Component component) {
    return componentRepository.save(component);
  }

  public List<LabelValue> findComponentTypeLabel() {
    return componentRepository.findAll().stream()
        .map(component -> new LabelValue(component.getId(),
            StringUtils.capitalize(component.getType().getDescricao())))
        .collect(Collectors.toList());
  }

  public List<LabelValue> findComponentCriticityLabel() {
    return componentRepository.findAll().stream()
        .map(component -> new LabelValue(component.getId(),
            StringUtils.capitalize(component.getCriticity().getDescricao())))
        .collect(Collectors.toList());
  }

}
