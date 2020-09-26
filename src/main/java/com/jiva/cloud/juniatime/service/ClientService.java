package com.jiva.cloud.juniatime.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.jiva.cloud.juniatime.dto.ClientDTO;
import com.jiva.cloud.juniatime.dto.ClientFilter;
import com.jiva.cloud.juniatime.dto.LabelValue;
import com.jiva.cloud.juniatime.exception.ClientEmUsoException;
import com.jiva.cloud.juniatime.exception.ClientInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.repository.CheckRepository;
import com.jiva.cloud.juniatime.repository.ClientRepository;

/**
 * @author highlander.dantas
 */
@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private CheckRepository checkRepository;

  /**
   * 
   * @return lista de Clientes
   */
  public List<Client> findAll() {
    return clientRepository.findAll();
  }

  /**
   * 
   * @param clientFilter parametros de busca
   * @param pageable
   * @return lista de clientes paginada
   */
  public Page<ClientDTO> filter(ClientFilter clientFilter, Pageable pageable) {
    return clientRepository.filter(clientFilter, pageable);
  }

  /**
   * 
   * @param id de um cliente
   * @return um cliente
   */
  public Client findOne(String id) {
    Optional<Client> client = clientRepository.findById(id);
    if (!client.isPresent()) throw new ClientInexistenteOuInvalidoException();
    
    return client.get();
  }

  /**
   * 
   * @return q quantidade de clientes ativos
   */
  public long count() {
    return clientRepository.countByEnabledTrue();
  }

  /**
   * 
   * @param id
   * @param client 
   * @return um cliente
   */
  public Client update(String id, Client client) {
    Client savedClient = findOne(id);
    BeanUtils.copyProperties(client, savedClient, "id", "createDate");
    savedClient.setLastChange(LocalDateTime.now());
    return clientRepository.save(savedClient);
  }

  /**
   * 
   * @param id 
   * 
   * Desativa um cliente
   */
  public void delete(String id) {
    Client clientSave = findOne(id);
    if (checkRepository.existsByClientId(findOne(id)))
      throw new ClientEmUsoException();
    clientSave.setEnabled(false);
    clientSave.setLastChange(LocalDateTime.now());

    removeCheckByClient(clientSave);
    clientRepository.save(clientSave);
  }

  /**
   * 
   * @param clientSave remove todos check de um cliente
   */
  private void removeCheckByClient(Client clientSave) {
    List<Check> checksByClient = checkRepository.findAllByClientId(clientSave);
    checksByClient.forEach(check -> 
      checkRepository.delete(check)
    );
  }

  /**
   * 
   * @param client
   * @return um cliente salvo
   */
  public Client save(Client client) {
    client.setCreateDate(LocalDateTime.now());
    client.setLastChange(LocalDateTime.now());
    if (client.getEnabled() == null)
      client.setEnabled(true);
    return clientRepository.save(client);
  }

  /**
   * 
   * @return uma lista de clientes adaptada pra dropdowns
   */
  public List<LabelValue> findClientLabel() {
    return clientRepository.findAllByEnabledTrue().stream().map(client -> new LabelValue(client.getId(), StringUtils.capitalize(client.getName())))
        .collect(Collectors.toList());
  }

}
