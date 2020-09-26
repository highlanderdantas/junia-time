package com.jiva.cloud.juniatime.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.jiva.cloud.juniatime.config.annotation.RestApiController;
import com.jiva.cloud.juniatime.dto.GenericResponse;
import com.jiva.cloud.juniatime.enums.TimeEnum;
import com.jiva.cloud.juniatime.service.AvailabilityService;
import com.jiva.cloud.juniatime.service.ScheduleService;

@RestApiController("/availability")
public class AvailabilityResource {

  @Autowired
  private AvailabilityService availabilityService;
  
  @Autowired
  private ScheduleService scheduleService;

  @GetMapping(value = "/findTagAvailability/{time}/{tagId}")
  public ResponseEntity<GenericResponse> findTagAvailability(@PathVariable TimeEnum time, @PathVariable String tagId,
      @RequestParam(required = false, defaultValue = "false") Boolean calculeNow){
    return ResponseEntity.ok(availabilityService.findTagAvailability(time, tagId, calculeNow));
  }

  @GetMapping("/findCheckAvailability/{time}/{checkId}")
  public ResponseEntity<GenericResponse> findCheckAvailability(@PathVariable TimeEnum time, @PathVariable String checkId,
      @RequestParam(required = false, defaultValue = "false") Boolean calculeNow){
    return ResponseEntity.ok(availabilityService.findCheckAvailability(time, checkId, calculeNow));
  }
  
  @GetMapping("/deleteDuplicateMonth7")
  public ResponseEntity<Void> deleteDuplicateMonth7() {
    scheduleService.deleteDuplicateMonth7();
    return ResponseEntity.ok().build();
  }

}
