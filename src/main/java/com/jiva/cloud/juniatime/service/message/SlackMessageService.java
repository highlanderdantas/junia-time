package com.jiva.cloud.juniatime.service.message;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.seratch.jslack.Slack;
import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty;
import com.jiva.cloud.juniatime.event.StatusCheckEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackMessageService implements MessageService {

	@Autowired
	private JuniaTimeProperty property;

	@Override
	public void send(StatusCheckEvent event) {
		var url = property.getWebhook().getUrl();
		var message = buildMessage(event, url);
		var payload = buildPayload(message);

		Slack slack = Slack.getInstance();
		try {
			slack.send(url, payload);
		} catch (IOException e) {
			log.error("Erro ao enviar mensagem para slack", e);
		}

	}

}
