package com.jiva.cloud.juniatime.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.jiva.cloud.juniatime.service.CheckService;
import com.jiva.cloud.juniatime.service.ScheduleService;
import com.jiva.cloud.juniatime.service.TagService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleConfig {
  
    private static final String FALHA_AO_INCREMENTAR_PINGCOUNT =  "Falha ao incrementar pingcount ";
    private static final String FALHA_AO_INCREMENTAR_PINGCOUNT_DO_CHECK = FALHA_AO_INCREMENTAR_PINGCOUNT + "do check.";
    private static final String FALHA_AO_INCREMENTAR_PINGCOUNT_DA_TAG = FALHA_AO_INCREMENTAR_PINGCOUNT + "da tag.";

    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private CheckService checkService;
    
    @Autowired
    private TagService tagService;

    @Async
    @Scheduled(fixedRateString = "${junia.check.interval}")
    public void pingOnChecks() {
        scheduleService.pingOnChecks();
    }
    
    //At minute 0.
    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void incrementPingDay() {
      checkService.findAll()
        .forEach(check -> scheduleService.incrementPingDay(check));
    }
    
    //At 00:00.
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void incrementPingMonth() {
      checkService.findAll()
        .forEach(check -> {
          try {
            scheduleService.incrementPingCheckMonth(check);
          } catch (Exception e) {
            log.error(FALHA_AO_INCREMENTAR_PINGCOUNT_DO_CHECK, e);
          }
        });
      
      tagService.findAll()
        .forEach(tag -> {
          try {
            scheduleService.incrementPingTagMonth(tag);
          } catch (Exception e) {
            log.error(FALHA_AO_INCREMENTAR_PINGCOUNT_DA_TAG, e);
          }
        });
    }
    
    //At 00:00 on day-of-month 1.
    @Async
    @Scheduled(cron = "0 0 0 1 * *")
    public void incrementPingYear() {
      checkService.findAll()
        .forEach(check -> {
          try {
            scheduleService.incrementPingCheckYear(check);
          } catch (Exception e) {
            log.error(FALHA_AO_INCREMENTAR_PINGCOUNT_DO_CHECK, e);
          }
        });
      
      tagService.findAll()
        .forEach(tag -> {
          try {
            scheduleService.incrementPingTagYear(tag);
          } catch (Exception e) {
            log.error(FALHA_AO_INCREMENTAR_PINGCOUNT_DA_TAG, e);
          }
        });
    }
    
    //At 00:00 on day-of-month 1 in January.
    @Async
    @Scheduled(cron = "0 0 0 1 1 *")
    public void saveYearAvailability() {
      checkService.findAll()
        .forEach(check -> scheduleService.saveYearCheckAvailability(check));
      
      tagService.findAll()
        .forEach(tag -> {
          try {
            scheduleService.saveYearTagAvailability(tag);
          } catch (Exception e) {
            log.error("Falha ao salvar disponibilidade da tag por ano.", e);
          }
        });
    }
}
