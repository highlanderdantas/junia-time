package com.jiva.cloud.juniatime.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiva.cloud.juniatime.dto.Availability;
import com.jiva.cloud.juniatime.dto.GenericResponse;
import com.jiva.cloud.juniatime.enums.TimeEnum;
import com.jiva.cloud.juniatime.exception.CheckInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.exception.TagInexistenteOuInvalidaException;
import com.jiva.cloud.juniatime.exception.TimeEnumInexistenteOuInvalidoException;
import com.jiva.cloud.juniatime.model.Check;
import com.jiva.cloud.juniatime.model.Tag;
import com.jiva.cloud.juniatime.model.ping.PingCount;
import com.jiva.cloud.juniatime.model.ping.PingCountCheckYear;
import com.jiva.cloud.juniatime.model.ping.PingCountTagYear;
import com.jiva.cloud.juniatime.utils.PingCountUtil;

@Service
public class AvailabilityService extends TagRepositories{
  
  private static final String AVAILABILITIES = "availabilities";

  @Autowired
  private ScheduleService scheduleService;

  /**
   * verifica se irá calcular os valores até o momento ou irá retorna somente os salvos
   */
  public GenericResponse findTagAvailability(TimeEnum time, String tagId, Boolean calculeNow) {
    if (calculeNow) return findTagNowAvailability(time, tagId);
    else return findTagAvailability(time, tagId);
  }
  
  /**
   * verifica se irá calcular os valores até o momento ou irá retorna somente os salvos
   */
  public GenericResponse findCheckAvailability(TimeEnum time, String checkId, Boolean calculeNow) {
    if (calculeNow) return findCheckNowAvailability(time, checkId);
    else return findCheckAvailability(time, checkId);
  }
  
  /**
   * Retorna as disponibilidades de acordo com o tipo time que pode ser dia, mês ou ano, de uma determinada tag.
   */
  public GenericResponse findTagAvailability(TimeEnum time, String tag) {  
    var year = ZonedDateTime.now().getYear();
    switch (time) {
      case DAY:
        return GenericResponse.build(AVAILABILITIES, tagAvailabilityDayRepository.findAllByTag(tag));
      case MONTH:
        return GenericResponse.build(AVAILABILITIES, tagAvailabilityMonthRepository.findAllByTagAndYear(tag, year));
      case YEAR:
        return GenericResponse.build(AVAILABILITIES, tagAvailabilityYearRepository.findAllByTagAndYear(tag, year));
      default:
        throw new TimeEnumInexistenteOuInvalidoException();
    }
  }

  /**
   * Retorna as disponibilidades de acordo com o tipo time que pode ser hora, dia, mês ou ano, de uma determinado check.
   */
  public GenericResponse findCheckAvailability(TimeEnum time, String check) {
    switch (time) {
      case HOUR:
        return GenericResponse.build(AVAILABILITIES, checkAvailabilityHourRepository.findAllByCheck(check));
      case DAY:
        return GenericResponse.build(AVAILABILITIES, checkAvailabilityDayRepository.findAllByCheck(check));
      case MONTH:
        return GenericResponse.build(AVAILABILITIES, checkAvailabilityMonthRepository.findAllByCheck(check));
      case YEAR:
        return GenericResponse.build(AVAILABILITIES, checkAvailabilityYearRepository.findAllByCheck(check));
      default:
        throw new TimeEnumInexistenteOuInvalidoException();
    }
  }
  
  
  /**
   * soma toda a disponibilidade dos meses do ano e todos os dias do
   * mês atual, para obter a disponibilidade do ano até o dia atual
   */
  public GenericResponse findTagNowAvailability(TimeEnum time, String tag) {
    Optional<Tag> savedTag = tagRepository.findById(tag);
    
    if (!savedTag.isPresent()) throw new TagInexistenteOuInvalidaException();
    
    PingCount pingCount = new PingCountTagYear(Arrays.asList(savedTag.get()));
    
    if (TimeEnum.MONTH.equals(time)) incrementByTagOfMonth(tag, pingCount);
    else if (TimeEnum.YEAR.equals(time)) incrementByTagOfYear(tag, pingCount, savedTag.get());
    else throw new TimeEnumInexistenteOuInvalidoException();
      
    Availability availability = scheduleService.calculeAvailability(pingCount, TimeEnum.YEAR);
    
    return GenericResponse.build(AVAILABILITIES, Arrays.asList(availability));
  }

  /**
   * soma toda a disponibilidade dos meses do ano e todos os dias do
   * mês atual, para obter a disponibilidade do ano até o dia atual
   */
  public GenericResponse findCheckNowAvailability(TimeEnum time, String check) {
    Optional<Check> savedCheck = checkRepository.findById(check);
    
    if (!savedCheck.isPresent()) throw new CheckInexistenteOuInvalidoException();
    
    PingCount pingCount = new PingCountCheckYear(savedCheck.get());
    
    switch (time) {
      case DAY:
        incrementByCheckOfDay(check, pingCount);
        break;
      case MONTH:
        incrementByCheckOfMonth(check, pingCount);
        break;
      case YEAR:
        incrementByCheckOfYear(check, pingCount);
        break;
      default:
        throw new TimeEnumInexistenteOuInvalidoException();
    }
    Availability availability = scheduleService.calculeAvailability(pingCount, TimeEnum.YEAR);
    
    return GenericResponse.build(AVAILABILITIES, Arrays.asList(availability));
  }
  
  
  /**
   * incrementa a tag para o calculo do mês
   */
  private void incrementByTagOfMonth(String tag, PingCount pingCount) {
    ZonedDateTime now = ZonedDateTime.now();
    Integer year = now.getYear();
    Integer month = now.getMonthValue();

    tagAvailabilityDayRepository.findAllByTagAndYearAndMonth(tag, year, month)
    .forEach(pingCountDay -> {
      PingCountUtil.incrementUp(pingCountDay.getUpCount(), pingCountDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(pingCountDay.getDownCount(), pingCountDay.getDownComercialCount(), pingCount);
    });
  }
  
  /**
   * incrementa a tag para o calculo do ano
   */
  private void incrementByTagOfYear(String tag, PingCount pingCount, Tag savedTag) {
    Integer year = ZonedDateTime.now().getYear();

    tagAvailabilityMonthRepository.findAllByTagAndYear(tag, year)
    .forEach(tagAvailabilityMonth -> {
      PingCountUtil.incrementUp(tagAvailabilityMonth.getUpCount(), tagAvailabilityMonth.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(tagAvailabilityMonth.getDownCount(), tagAvailabilityMonth.getDownComercialCount(), pingCount);
    });
    
    pingCountTagDayRepository.findByTagsIn(Arrays.asList(savedTag))
    .forEach(pingCountDay -> {
      PingCountUtil.incrementUp(pingCountDay.getUpCount(), pingCountDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(pingCountDay.getDownCount(), pingCountDay.getDownComercialCount(), pingCount);
    });
  }

  /**
   * incrementa o check para o calculo do ano
   */
  private void incrementByCheckOfYear(String check, PingCount pingCount) {
    Integer year = ZonedDateTime.now().getYear();

    checkAvailabilityMonthRepository.findAllByCheckAndYear(check, year)
    .forEach(checkAvailabilityMonth -> {
      PingCountUtil.incrementUp(checkAvailabilityMonth.getUpCount(), checkAvailabilityMonth.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityMonth.getDownCount(), checkAvailabilityMonth.getDownComercialCount(), pingCount);
    });
    
    pingCountCheckMonthRepository.findAllByCheck(check)
    .forEach(checkAvailabilityDay -> {
      PingCountUtil.incrementUp(checkAvailabilityDay.getUpCount(), checkAvailabilityDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityDay.getDownCount(), checkAvailabilityDay.getDownComercialCount(), pingCount);
    });
  }

  /**
   * incrementa o check para o calculo do mês
   */
  private void incrementByCheckOfMonth(String check, PingCount pingCount) {
    ZonedDateTime now = ZonedDateTime.now();
    Integer year = now.getYear();
    Integer month = now.getMonthValue();

    checkAvailabilityDayRepository.findAllByCheckAndYearAndMonth(check, year, month)
    .forEach(checkAvailabilityDay -> {
      PingCountUtil.incrementUp(checkAvailabilityDay.getUpCount(), checkAvailabilityDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityDay.getDownCount(), checkAvailabilityDay.getDownComercialCount(), pingCount);
    });
    pingCountCheckDayRepository.findAllByCheck(check)
    .forEach(checkAvailabilityDay -> {
      PingCountUtil.incrementUp(checkAvailabilityDay.getUpCount(), checkAvailabilityDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityDay.getDownCount(), checkAvailabilityDay.getDownComercialCount(), pingCount);
    });
  }

  /**
   * incrementa o check para o calculo do dia
   */
  private void incrementByCheckOfDay(String check, PingCount pingCount) {
    ZonedDateTime now = ZonedDateTime.now();
    Integer year = now.getYear();
    Integer month = now.getMonthValue();
    Integer day = now.getDayOfMonth();

    checkAvailabilityHourRepository.findAllByCheckAndYearAndMonthAndDay(check, year, month, day)
    .forEach(checkAvailabilityHour -> {
      PingCountUtil.incrementUp(checkAvailabilityHour.getUpCount(), checkAvailabilityHour.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityHour.getDownCount(), checkAvailabilityHour.getDownComercialCount(), pingCount);
    });
    pingCountCheckHourRepository.findAllByCheck(check)
    .forEach(checkAvailabilityDay -> {
      PingCountUtil.incrementUp(checkAvailabilityDay.getUpCount(), checkAvailabilityDay.getUpComercialCount(), pingCount);
      PingCountUtil.incrementDown(checkAvailabilityDay.getDownCount(), checkAvailabilityDay.getDownComercialCount(), pingCount);
    });
  }
}
  
