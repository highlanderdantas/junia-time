package com.jiva.cloud.juniatime.event.listener;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.jiva.cloud.juniatime.event.StatusCheckEvent;
import com.jiva.cloud.juniatime.service.message.MessageService;

@Component
public class SlackListener {
  
    @Autowired
    private MessageService messageService;
        
    @EventListener
    public void sendEvent(StatusCheckEvent event) throws URISyntaxException{
      this.messageService.send(event);
    }
}
