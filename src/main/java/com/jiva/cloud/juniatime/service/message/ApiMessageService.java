package com.jiva.cloud.juniatime.service.message;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty;
import com.jiva.cloud.juniatime.event.StatusCheckEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiMessageService implements MessageService {

	@Autowired
	private JuniaTimeProperty property;

	@Override
	public void send(StatusCheckEvent event) {
		var url = property.getWebhook().toString();
		var message = buildMessage(event, url);
		var payload = buildPayload(message);

		doPost(payload, message);
	}

	private void doPost(String payload, Message message) {
		RestTemplate restTemplate = new RestTemplate();

		var params = new HashMap<String, String>();
		params.put("clientValue", message.getClient());
		params.put("productValue", message.getProduct());
		params.put("statusValue", message.getStatus());

		var url = buildUrl(message.getUrl());
		ResponseEntity<String> result = restTemplate
				.postForEntity(url, payload, String.class, params);
		log.info(result.toString());
	}

	private String buildUrl(String webHook) {
		return new StringBuilder()
				.append(webHook)
				.append("?client={clientValue}")
				.append("&product={productValue}")
				.append("&status={statusValue}")
			.toString();
	}

}
