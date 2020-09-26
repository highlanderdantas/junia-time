package com.jiva.cloud.juniatime.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiva.cloud.juniatime.dto.Availability;
import com.jiva.cloud.juniatime.dto.ResponseTimeout;
import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.enums.TimeEnum;
import com.jiva.cloud.juniatime.exception.PingCountInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityDay;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityHour;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityMonth;
import com.jiva.cloud.juniatime.model.availability.CheckAvailabilityYear;
import com.jiva.cloud.juniatime.model.availability.TagAvailabilityDay;
import com.jiva.cloud.juniatime.model.availability.TagAvailabilityMonth;
import com.jiva.cloud.juniatime.model.availability.TagAvailabilityYear;
import com.jiva.cloud.juniatime.model.ping.PingCount;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckDay;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckHour;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckMonth;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckYear;
import com.jiva.cloud.juniatime.model.ping.PingCountTagDay;
import com.jiva.cloud.juniatime.model.ping.PingCountTagMonth;
import com.jiva.cloud.juniatime.model.ping.PingCountTagYear;
import com.jiva.cloud.juniatime.utils.PingCountUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ScheduleService extends TagRepositories {
  
  
  @Autowired
  private CheckService checkService;

  /**
   * Lista todos os checks e verifica o estado da aplicação, caso esteja down
   * chama o alerta do slack, basicamente fica monitorando as os @{@link Check}
   * (aplicações) nos avisando de suas mudanças de estados
   */
  public void pingOnChecks() {
    
    List<Check> checks = checkService.findAll();

    checks.forEach(checkId -> {
      Check check = checkService.findOne(checkId.getId());

      if (!StatusEnum.PAUSED.equals(check.getStatus())) {
        ResponseTimeout responseAndTimeout = this.executePingCheck(check);

        if (responseAndTimeout != null && responseAndTimeout.getCodeStatus() != null
            && responseAndTimeout.getCodeStatus() != 200) {

          check = checkService.incrementErrorCount(check.getId());
        } else if (check.getStatus() == StatusEnum.DOWN && responseAndTimeout.getCodeStatus() != null
            && responseAndTimeout.getCodeStatus() == 200) {

          check = checkService.resetErrorCount(check.getId(), check.getErrorCount());
        }
        if (check.getSavePing() && responseAndTimeout.getCodeStatus() != null) {
          this.incrementPingHour(check, responseAndTimeout);
        }
      }
    });
  }
  
  /**
  *
  * @param check
  * @return o codeStatus da ultima verificação e o tempo de resposta Check uma url e verifica seu estado
  */
 private ResponseTimeout executePingCheck(Check check) {
   ResponseTimeout responseTimeut = new ResponseTimeout();

   try {
     String formedUrl = new StringBuilder()
                           .append(check.getType().getDescricao())
                           .append("://")
                           .append(check.getUrl().toLowerCase())
                          .toString();

     long startTime = System.currentTimeMillis();
     HttpURLConnection connection = this.buildConnection(formedUrl);

     String redirect = connection.getHeaderField("Location");
     if (redirect != null) {
       connection = this.buildConnection(redirect);
     }

     responseTimeut.setResponseTime((System.currentTimeMillis() - startTime));
     responseTimeut.setCodeStatus((long) connection.getResponseCode());

     log.info("{} status code: {}", formedUrl, responseTimeut.getCodeStatus());

   } catch (SocketTimeoutException e) {
     responseTimeut.setCodeStatus(999l);
     log.error("Timeout excedido ao tentar GET na URL: {}", check.getUrl());
   } catch (Exception e) {
     log.error("Não foi possivel verificar a url: {}", check.getUrl());
   }
   return responseTimeut;
 }
 
 /**
 *
 * @param url a ser verificada
 * @return @{@link HttpURLConnection}
 * @throws IOException
 * @throws MalformedURLException
 * @throws ProtocolException
 *
 */
private HttpURLConnection buildConnection(String url) throws IOException, MalformedURLException, ProtocolException {
  HttpURLConnection connection;
  connection = (HttpURLConnection) new URL(url).openConnection();
  connection.setRequestMethod("GET");
  connection.setConnectTimeout(5000);
  connection.setReadTimeout(5000);
  connection.connect();

  return connection;
}


  
  /**
   * calcula porcentagem
   * exemplo:
   * 200  --- > 100%
   * 50   --->  x
   * x = 50 * 100 / 200
   */
  private double calculePorcent(Long downCount, Long totalPings) {
    Double downPorcent = (downCount * 100L) / Double.valueOf(totalPings);
    if (downPorcent >= 100) return Double.valueOf(0);
    else return Double.valueOf(100) - downPorcent;
  }
  
  /**
   * calcula a disponibilidade baseado nos pings UP e nos DOWN
   */
  public Availability calculeAvailability(PingCount pingCount, TimeEnum time) {
    
    Long totalPings = pingCount.getUpCount() + pingCount.getDownCount();
    Long downCount = pingCount.getDownCount();

    Double percent = downCount == 0 
        ? Double.valueOf(100) 
        : calculePorcent(downCount, totalPings); 

    Long totalComercialPings = pingCount.getUpComercialCount() + pingCount.getDownComercialCount();
    Long downComercialCount = pingCount.getDownComercialCount();
    
    Double comercialPercent = downComercialCount == 0 
        ? Double.valueOf(100) 
        : calculePorcent(downComercialCount, totalComercialPings);
        
    ZonedDateTime lastChange = pingCount.getLastChange();

    Integer reference = null;
    switch(time) {
      case HOUR:
        reference = lastChange.getHour();
        break;
      case DAY:
        reference = lastChange.getDayOfMonth();
        break;
      case MONTH:
        reference = lastChange.getMonthValue();
        break;
      case YEAR:
        reference = lastChange.getYear();
    }

    if(reference != null) {
        return new Availability(percent,
            comercialPercent,
            pingCount.getUpCount(),
            pingCount.getDownCount(),
            pingCount.getUpComercialCount(),
            pingCount.getDownComercialCount(),
            reference, time);
    }

    return null;
  }

  

    
  /**
   * 
   * @param check
   * @param responseAndTimeout
   * count incrementado a cada minuto
   * primeira tabela de cálculo de disponibilidades.
   * se ping up, incrementa campo up da tabela; se down, incrementa campo down da tabela
   * aqui o metodo possui a responsabilidade apenas de incrementar pings up/down do check especificado.
   */
  @Transactional
  public void incrementPingHour(Check check, ResponseTimeout responseAndTimeout) {
    StatusEnum status = responseAndTimeout.getCodeStatus() == 200 ? StatusEnum.UP : StatusEnum.DOWN;

    Optional<PingCount> opPingCountHour = this.pingCountCheckHourRepository.findByCheckId(check);
    PingCount pingCountHour = opPingCountHour.orElse(new PingCountCheckHour(check));

    if(StatusEnum.UP.equals(status)) {
      pingCountHour.incrementUpCount();
      if(isHorarioComercial()) pingCountHour.incrementUpComercialCount();

    } else if(StatusEnum.DOWN.equals(status)) {
      pingCountHour.incrementDownCount();
      if(isHorarioComercial()) pingCountHour.incrementDownComercialCount();
    }

    this.pingCountCheckHourRepository.save((PingCountCheckHour) pingCountHour);

  }
  
  /**
   * horario comercial eh true se eh dia da semana das 7 as 19 ou sabado das 07 as 12.
   */
  private boolean isHorarioComercial() {
    ZonedDateTime now = ZonedDateTime.now();
    
    //1 -> segunda feira
    int dayOfWeek = now.getDayOfWeek().getValue(); 
        
    int hour = now.getHour();
    
    //horario em UTC
    if((dayOfWeek == 7 || hour < 10 || hour > 22))
      return false;
    if(dayOfWeek == 6 && (hour < 10 || hour > 15))
      return false;
    return true;
  }
  
  /**
   * registro feito a cada hora
   */
  @Transactional
  public void saveHourAvailability(Check check) {
    Optional<PingCount> opPingCount = this.pingCountCheckHourRepository.findByCheckId(check);
    if(opPingCount.isPresent()) {
      PingCount pingCountHour = opPingCount.get();

      Availability availability = this.calculeAvailability(pingCountHour, TimeEnum.HOUR);

      this.checkAvailabilityHourRepository.save(new CheckAvailabilityHour(availability, check.getId()));
    }
  }
  
  /**
   * calculo feito a cada hora
   * Le da tabela de disponibilidade por hora e incrementa esse valor
   * na disponibilidade por dia ja calculada para check especificado.
   * Apos a soma, disponibilidade da tabela por hora pode ser resetado, pois
   * o registro da disponibilidade daquela hora ja foi feito.
   */
  @Transactional
  public void incrementPingDay(Check check) {
    
    
    Optional<PingCount> opPingCountHour = this.pingCountCheckHourRepository.findByCheckId(check);
    if(opPingCountHour.isPresent()) {
      PingCount pingCountHour = opPingCountHour.get();
      
      //salva pingCount temporario para check
      this.saveTempCheckPingCountDay(check, pingCountHour);
      //salve pingCount temporario para tag
      this.saveTempTagPingCountDay(check.getTags(), pingCountHour);
      //registra a disponibilidade da hora...
      this.saveHourAvailability(check);
      //e reseta o calculo da tabela 'temporaria'
      this.pingCountCheckHourRepository.delete((PingCountCheckHour) pingCountHour);
    }
  }

  private void saveTempCheckPingCountDay(Check check, PingCount pingCountHour) {
    Optional<PingCount> opPingCountDay = this.pingCountCheckDayRepository.findByCheckId(check);

    if(opPingCountDay.isPresent()) {
      PingCount pingCountDay = opPingCountDay.get();
      
      PingCountUtil.incrementUp(pingCountHour.getUpCount(), pingCountHour.getUpComercialCount(), pingCountDay);
      PingCountUtil.incrementDown(pingCountHour.getDownCount(), pingCountHour.getDownComercialCount(), pingCountDay);
      this.pingCountCheckDayRepository.save((PingCountCheckDay) pingCountDay);
    } else {
      this.pingCountCheckDayRepository.save(new PingCountCheckDay(pingCountHour));
    }
  }
  
  private void saveTempTagPingCountDay(List<Tag> tags, PingCount pingCountHour) {
    List<PingCount> pingCountDays = this.pingCountTagDayRepository.findByTagsIn(tags);

    //TODO: modificar para considerar quando o check possuir mais que uma tag.
    if(!pingCountDays.isEmpty()) {
      PingCount pingCount = pingCountDays.get(0);
      
      PingCountUtil.incrementUp(pingCountHour.getUpCount(), pingCountHour.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(pingCountHour.getDownCount(), pingCountHour.getDownComercialCount(), pingCount);
      this.pingCountTagDayRepository.save((PingCountTagDay) pingCount);
    }
    else {
      this.pingCountTagDayRepository.save(new PingCountTagDay(pingCountHour));
    }
  }
  
  /**
   * registro feito a cada dia
   */
  @Transactional
  public void saveDayCheckAvailability(Check check) {
    Optional<PingCount> opPingCount = this.pingCountCheckDayRepository.findByCheckId(check);

    if (!opPingCount.isPresent())
      throw new PingCountInexistenteOuInvalidoException();
    
    PingCount pingCountHour = opPingCount.get();
    Availability availability = this.calculeAvailability(pingCountHour, TimeEnum.DAY);

    this.checkAvailabilityDayRepository.save(new CheckAvailabilityDay(availability, check.getId()));
  }
  
  /**
   * registro feito a cada dia
   */
  @Transactional
  public void saveDayTagAvailability(Tag tag) {
    List<PingCount> pingCount = this.pingCountTagDayRepository.findByTagsIn(Arrays.asList(tag));

    if (pingCount.isEmpty())
      throw new PingCountInexistenteOuInvalidoException(tag.getDescription());

    PingCount pingCountHour = pingCount.get(0);
    Availability availability = this.calculeAvailability(pingCountHour, TimeEnum.DAY);

    this.tagAvailabilityDayRepository.save(new TagAvailabilityDay(availability, tag.getId()));

  }
  
  /**
   * calculo feito a cada dia
   * Le da tabela de disponibilidade por dia e incrementa esse valor
   * na disponibilidade por mes ja calculada para check especificado.
   * Apos a soma, disponibilidade da tabela por dia pode ser resetado, pois
   * o registro da disponibilidade daquele dia ja foi feito.
   */
  @Transactional
  public void incrementPingCheckMonth(Check check) {
    Optional<PingCount> opPingCountCheckDay = this.pingCountCheckDayRepository.findByCheckId(check);

    if(opPingCountCheckDay.isPresent()) {

      PingCount pingCountCheckDay = opPingCountCheckDay.get();
      Optional<PingCount> opPingCountCheckMonth = this.pingCountCheckMonthRepository.findByCheckId(check);

      if(opPingCountCheckMonth.isPresent()) {
        PingCount pingCountCheckMonth = opPingCountCheckMonth.get();
        
        //incrementa disponibilidade check/mês
        PingCountUtil.incrementUp(pingCountCheckDay.getUpCount(), pingCountCheckDay.getUpComercialCount(), pingCountCheckMonth);
        PingCountUtil.incrementDown(pingCountCheckDay.getDownCount(), pingCountCheckDay.getDownComercialCount(), pingCountCheckMonth);
        this.pingCountCheckMonthRepository.save((PingCountCheckMonth) pingCountCheckMonth);
        
      } else {
        this.pingCountCheckMonthRepository.save(new PingCountCheckMonth(pingCountCheckDay));
      }
      //registra a disponibilidade do check/dia...
      this.saveDayCheckAvailability(check);
    }
  }
  
  @Transactional
  public void incrementPingTagMonth(Tag tag) {
    
    List<Check> checks = this.checkService.findAllByTagsId(tag.getId());
    List<PingCount> opPingCountTagMonth = this.pingCountTagMonthRepository.findByTagsIn(Arrays.asList(tag));
    
    
    PingCount pingCountTagMonth = opPingCountTagMonth.isEmpty() 
          ? new PingCountTagMonth(Arrays.asList(tag))
          : opPingCountTagMonth.get(0);
    
      //incrementa a quantidade somando todos os pings dos checks
      checks.forEach(check -> {
        Optional<PingCount> opPingCountCheckDay = this.pingCountCheckDayRepository.findByCheckId(check);

        if(opPingCountCheckDay.isPresent()) {
          PingCount pingCountCheckDay = opPingCountCheckDay.get();

          PingCountUtil.incrementUp(pingCountCheckDay.getUpCount(), pingCountCheckDay.getUpComercialCount(), pingCountTagMonth);
          PingCountUtil.incrementDown(pingCountCheckDay.getDownCount(), pingCountCheckDay.getDownComercialCount(), pingCountTagMonth);
          
          //reseta o calculo da tabela check 'temporaria', para iniciar o calculo de um novo dia
          this.pingCountCheckDayRepository.delete((PingCountCheckDay) pingCountCheckDay);
        }
      });
      
      //salva a contagem de pings do mes
      this.pingCountTagMonthRepository.save((PingCountTagMonth) pingCountTagMonth);
      
      //registra a disponibilidade
      this.saveDayTagAvailability(tag);
  }
  
  /**
   * registro feito a cada mês
   */
  @Transactional
  public void saveMonthCheckAvailability(Check check) {

    Optional<PingCount> opPingCount = this.pingCountCheckMonthRepository.findByCheckId(check);
    if (!opPingCount.isPresent()) throw new PingCountInexistenteOuInvalidoException();

    PingCount pingCountHour = opPingCount.get();
    Availability availability = this.calculeAvailability(pingCountHour, TimeEnum.MONTH);

    this.checkAvailabilityMonthRepository.save(new CheckAvailabilityMonth(availability, check.getId()));
  }
  
  /**
   * registro feito a cada mês
   */
  @Transactional
  public void saveMonthTagAvailability(Tag tag) {

    List<PingCount> pingCount = this.pingCountTagMonthRepository.findByTagsIn(Arrays.asList(tag));
    if (pingCount.isEmpty()) throw new PingCountInexistenteOuInvalidoException(tag.getDescription());

    PingCount pingCountMonth = pingCount.get(0);
    Availability availability = this.calculeAvailability(pingCountMonth, TimeEnum.MONTH);

    this.tagAvailabilityMonthRepository.save(new TagAvailabilityMonth(availability, tag.getId()));

  }
  
  /**
   * calculo feito a cada mes
   * Le da tabela de disponibilidade por mes e incrementa esse valor
   * na disponibilidade por ano ja calculada para check especificado.
   * Apos a soma, disponibilidade da tabela por mes pode ser resetado, pois
   * o registro da disponibilidade daquele mes ja foi feito.
   */
  @Transactional
  public void incrementPingCheckYear(Check check) {
    Optional<PingCount> opPingCountMonth = this.pingCountCheckMonthRepository.findByCheckId(check);

    if(opPingCountMonth.isPresent()) {
      PingCount pingCountMonth = opPingCountMonth.get();
      Optional<PingCount> opPingCountYear = this.pingCountCheckYearRepository.findByCheckId(check);

      if(opPingCountYear.isPresent()) {
        PingCount pingCountYear = opPingCountYear.get();

        PingCountUtil.incrementUp(pingCountMonth.getUpCount(), pingCountMonth.getUpComercialCount(), pingCountYear);
        PingCountUtil.incrementDown(pingCountMonth.getDownCount(), pingCountMonth.getDownComercialCount(), pingCountYear);
        
        this.pingCountCheckYearRepository.save((PingCountCheckYear) pingCountYear);
      } else {
        this.pingCountCheckYearRepository.save(new PingCountCheckYear(pingCountMonth));
      }
      //registra a disponibilidade do mes...
      this.saveMonthCheckAvailability(check);
    }
  }
  
  //
  /**
   * calculo feito a cada mes
   */
  @Transactional
  public void incrementPingTagYear(Tag tag) {
    List<Check> checks = this.checkService.findAllByTagsId(tag.getId());
    List<PingCount> opPingCountTagYear = this.pingCountTagYearRepository.findByTagsIn(Arrays.asList(tag));
    
    PingCount pingCountTagYear = opPingCountTagYear.isEmpty()
        ? new PingCountTagYear(Arrays.asList(tag))
        : opPingCountTagYear.get(0);
    
    
      //incrementa a quantidade somando todos os pings dos checks
      checks.forEach(check -> {
        Optional<PingCount> opPingCountCheckMonth = this.pingCountCheckMonthRepository.findByCheckId(check);

        if(opPingCountCheckMonth.isPresent()) {
          PingCount pingCountCheckMonth = opPingCountCheckMonth.get();

          PingCountUtil.incrementUp(pingCountCheckMonth.getUpCount(), pingCountCheckMonth.getUpComercialCount(), pingCountTagYear);
          PingCountUtil.incrementDown(pingCountCheckMonth.getDownCount(), pingCountCheckMonth.getDownComercialCount(), pingCountTagYear);

          //reseta o calculo da tabela check 'temporaria', para iniciar o calculo de um novo dia
          this.pingCountCheckMonthRepository.delete((PingCountCheckMonth) pingCountCheckMonth);
        }
      });
      
      //registra a contagem de pings do mes
      this.pingCountTagYearRepository.save((PingCountTagYear) pingCountTagYear);
      
      //registra a disponibilidade
      this.saveMonthTagAvailability(tag);
  }
  
  /**
   * registro feito a cada ano
   */
  @Transactional
  public void saveYearCheckAvailability(Check check) {
    Optional<PingCount> opPingCount = this.pingCountCheckMonthRepository.findByCheckId(check);
    if(opPingCount.isPresent()) {
      PingCount pingCountYear = opPingCount.get();
      
      Availability availability = this.calculeAvailability(pingCountYear, TimeEnum.YEAR);
      
      this.checkAvailabilityYearRepository.save(new CheckAvailabilityYear(availability, check.getId()));
      
      //limpa disponibilidade para acumular pings de um novo ano
      this.pingCountCheckYearRepository.deleteById( pingCountYear.getId());
    }
  }
  
  /**
   * registro feito a cada ano
   */
  @Transactional
  public void saveYearTagAvailability(Tag tag) {
    List<PingCount> pingCount = this.pingCountTagYearRepository.findByTagsIn(Arrays.asList(tag));
    if (pingCount.isEmpty()) throw new PingCountInexistenteOuInvalidoException(tag.getDescription());
    
    PingCount pingCountYear = pingCount.get(0);
    Availability availability = this.calculeAvailability(pingCountYear, TimeEnum.YEAR);

    this.tagAvailabilityYearRepository.save(new TagAvailabilityYear(availability, tag.getId()));

    //limpa disponibilidade para acumular pings de um novo ano
    this.pingCountTagYearRepository.deleteById( pingCountYear.getId());
  }
  
  
  
  
  public void deleteDuplicateMonth7() {
    
    var checks = checkService.findAll();
    
    checks.forEach(check -> {
      List<CheckAvailabilityMonth> availabilityMonths = this.checkAvailabilityMonthRepository.findAllByCheckAndDate(check.getId(), "7/2020");
      
      if (availabilityMonths.size() == 2) {
        if (availabilityMonths.get(0).getValue() > availabilityMonths.get(1).getValue()) {
//          log.info("deletando o check, {}", availabilityMonths.get(1).getId());
          checkAvailabilityMonthRepository.deleteById(availabilityMonths.get(1).getId());
        } else {
//          log.info("deletando o check, {}", availabilityMonths.get(0).getId());
          checkAvailabilityMonthRepository.deleteById(availabilityMonths.get(0).getId());
        }
      }
    });
  }

}
