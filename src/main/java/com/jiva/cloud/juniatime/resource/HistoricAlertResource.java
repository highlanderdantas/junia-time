package com.jiva.cloud.juniatime.resource;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.model.HistoricAlert;
import com.jiva.cloud.juniatime.service.HistoricAlertService;

@RestApiController("/historic-alert")
public class HistoricAlertResource {

  @Autowired
  private HistoricAlertService historicAlertService;

  @GetMapping("/findAllLastFourHours")
  public ResponseEntity<List<HistoricAlert>> findAllLastFourHours(@RequestParam(required = false) String client) { 
    return ResponseEntity.ok(historicAlertService.findAllLastFourHours(client));
  }
}
