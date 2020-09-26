package com.jiva.cloud.juniatime.service;

import java.time.ZonedDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiva.cloud.juniatime.model.HistoricAlert;
import com.jiva.cloud.juniatime.repository.HistoricAlertRepository;

@Service
public class HistoricAlertService {
  
  @Autowired
  private HistoricAlertRepository historicAlertRepository;

  public List<HistoricAlert> findAllLastFourHours(String client) {
    ZonedDateTime lastFourHours = ZonedDateTime.now().minusHours(4);

    if (StringUtils.isEmpty(client)) 
      return historicAlertRepository.findAllByDateEventGreaterThan(lastFourHours);
    else
      return historicAlertRepository.findAllByClientContainingIgnoreCaseAndDateEventGreaterThan(client, lastFourHours);
    
  }

}
