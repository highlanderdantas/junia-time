package com.jiva.cloud.juniatime.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jiva.cloud.juniatime.dto.CheckFilter;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.event.CreatedResourceEvent;
import com.jiva.cloud.juniatime.event.StatusCheckEvent;
import com.jiva.cloud.juniatime.exception.CheckInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Client;
import com.jiva.cloud.juniatime.model.Product;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.availability.CheckAvailability;
import com.jiva.cloud.juniatime.repository.CheckRepository;
import com.jiva.cloud.juniatime.repository.availability.CheckAvailabilityRepository;

/**
 * @author highlander.dantas
 */
@Service
public class CheckService {

  private static final String ERROR_ID_IMCOMPATIVEL_COM_CHECK = "O id do client ou do produto não batem com mesmo check, tente com outros valores!!";

  @Autowired
  private CheckRepository checkRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private TagService tagService;
 
  @Autowired
  private ClientService clientService;
  
  @Autowired
  private ProductService productService;
  
  @Autowired
  private CheckAvailabilityRepository checkAvailabilityRepository;
  
  
  /**
   * 
   * @param id
   * @param check
   * @return um check atualizado
   */
  public Check update(String id, Check check) {
    Check savedCheck = findOne(id);
    BeanUtils.copyProperties(check, savedCheck, "id", "errorCount", "createDate", "interval");
    savedCheck.setLastChange(ZonedDateTime.now());
    return save(savedCheck);
  }

  /**
   * 
   * @param id
   * @return um check especifico
   */
  public Check findOne(String id) {
    Optional<Check> check = checkRepository.findById(id);
    if (!check.isPresent())
      throw new CheckInexistenteOuInvalidoException();
    return check.get();
  }

  /**
   * 
   * @param checkFilter parametros de busca
   * @return uma lista de check baseado em um  cliente e produtos
   */
  public List<Check> findAllByClientIdAndProductIdIn(CheckFilter checkFilter) {
    if (checkFilter.getClientId() == null || checkFilter.getProducts() == null)
      throw new CheckInexistenteOuInvalidoException();

    Client client = new Client();
    client.setId(checkFilter.getClientId());

    return checkRepository.findAllByClientIdAndProductIdIn(client, checkFilter.getProducts());
  }

  /**
   * 
   * @param checkFilter parametros de busca
   * @return checks de determinado cliente
   */
  public List<Check> findAllByClientId(CheckFilter checkFilter) {
    if (checkFilter.getClientId() == null)
      throw new CheckInexistenteOuInvalidoException();

    Client client = new Client();
    client.setId(checkFilter.getClientId());

    return checkRepository.findAllByClientId(client);
  }

  /**
   * @return uma lista de checks
   */
  public List<Check> findAll() {
    return checkRepository.findAll();
  }


  public long count() {
    return checkRepository.count();
  }

  /**
   * 
   * @param check
   * @param response
   * @return uma check salvo
   */
  public Check create(Check check, HttpServletResponse response) {
    check.setCreateDate(ZonedDateTime.now());
    check.setLastChange(ZonedDateTime.now());

    if (check.getErrorCount() == null) {
      check.setErrorCount(new AtomicLong(0));
    }
    if (check.getAlerTreshold() == null) {
      check.setAlerTreshold(1l);
    }
    Check savedCheck = save(check);
    publisher.publishEvent(new CreatedResourceEvent(this, response, savedCheck.getId()));
    return savedCheck;
  }

  /**
   * 
   * @param id
   * @return 
   * 
   * Incrementa a contagem de erros de um check
   */
  public Check incrementErrorCount(String id) {
    Check check = this.findOne(id);
    check.setStatus(StatusEnum.DOWN);
    check.setErrorCount(new AtomicLong(check.getErrorCount().incrementAndGet()));
    check.setLastChange(ZonedDateTime.now());

    if (check.getErrorCount().get() == check.getAlerTreshold()) {
      publisher.publishEvent(new StatusCheckEvent(check, StatusEnum.DOWN));
    }

    return save(check);
  }


  /**
   *  Salva um check
   * @param check
   * @return retorna um check salvo
   */
  public Check save(Check check) {
    return checkRepository.save(check);
  }

  /**
   * 
   * @param id
   * @return um check com a contagem de erros removida
   */
  public Check resetErrorCount(String id, AtomicLong lastErrorCount) {
    Check check = this.findOne(id);
    check.setStatus(StatusEnum.UP);
    check.setErrorCount(new AtomicLong(0));
    check.setLastChange(ZonedDateTime.now());

    if (lastErrorCount.get() >= check.getAlerTreshold()) {
      publisher.publishEvent(new StatusCheckEvent(check, StatusEnum.UP));
    }

    return save(check);
  }

  /**
   * 
   * @param id
   * 
   * Deleta um check
   */
  public void delete(String id) {
    checkRepository.deleteById(id);
  }

  /**
   * 
   * @param status
   * @return quuantidade de check em um status
   */
  public long countByStatus(StatusEnum status) {
    return checkRepository.countByStatus(status);
  }

  /**
   * 
   * @param checkFilter parametros de busca
   * @param pageable
   * @return uma lista de checks paginadas
   */
  public Page<Check> filter(CheckFilter checkFilter, Pageable pageable) {
    return checkRepository.filter(checkFilter, pageable);
  }

  /**
   * 
   * @param id 
   * @return uma lista de string
   */
  public List<Check> findAllByTagsId(String id) {
    Tag tag = tagService.findOne(id);
    return checkRepository.findAllByTagsId(tag);
  }

  /**
   * Atualiza o status de um check pelo seu id, e publica o acontecimento no publisher.
   * @param id Id do check que terá o status atualizado
   * @param status Novo status do check
   * @return Um check com status atualizado.
   */
  public Check updateStatus(String id, StatusEnum status) {
    Check check = findOne(id);
    check.setStatus(status);

    if (StatusEnum.UP.equals(status)) {
      check.setStatus(StatusEnum.UP);
      check.setErrorCount(new AtomicLong(0));
      check.setLastChange(ZonedDateTime.now());

      publisher.publishEvent(new StatusCheckEvent(check, StatusEnum.RESTARTED));
    } else {

      publisher.publishEvent(new StatusCheckEvent(check, status));
    }
    return save(check);
  }
  
  public CheckAvailability findAvailabilityByCheck(String checkId) {
    Check check = findOne(checkId);

    String availabilityDescription = 
        new StringBuilder()
          .append(check.getClient().getName())
          .append(" - ")
          .append(check.getProduct().getName())
        .toString();
    
    Optional<CheckAvailability> checkOpt = checkAvailabilityRepository.findById(availabilityDescription);
    if (!checkOpt.isPresent()) throw new IllegalArgumentException("Erro ao buscar Check Availability");

    return checkOpt.get();

  }

  public Check findByClientIdAndProduct(String clientId, String productId) {
    Client client = clientService.findOne(clientId);
    Product product = productService.findOne(productId);

    Optional<Check> check = checkRepository
        .findByClientIdAndProductId(client, product);

    if (!check.isPresent()) throw new CheckInexistenteOuInvalidoException(ERROR_ID_IMCOMPATIVEL_COM_CHECK);
    return check.get();
  }
  
}
;