package com.jiva.cloud.juniatime.service.message;

import org.springframework.beans.factory.annotation.Autowired;

import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty;
import com.jiva.cloud.juniatime.event.StatusCheckEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalMessageService implements MessageService {

	@Autowired
	private JuniaTimeProperty property;

	@Override
	public void send(StatusCheckEvent event) {
		var url = property.getWebhook().getUrl();
		var message = buildMessage(event, url);

		log.info("{} - {} is {}", message.getClient(), message.getProduct(), message.getStatus());

	}

}
