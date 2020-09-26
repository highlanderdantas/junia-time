package com.jiva.cloud.juniatime.config.message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty;
import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty.TypeMessage;
import com.jiva.cloud.juniatime.service.message.ApiMessageService;
import com.jiva.cloud.juniatime.service.message.LocalMessageService;
import com.jiva.cloud.juniatime.service.message.MessageService;
import com.jiva.cloud.juniatime.service.message.SlackMessageService;

@Configuration
public class MessageConfig {

	private TypeMessage type;

	MessageConfig(JuniaTimeProperty property) {
		this.type = property.getTypeMessage();
	}

	@Bean
	public MessageService messageService() {
		if (TypeMessage.SLACK.equals(this.type)) {
			return new SlackMessageService();
		} else if (TypeMessage.API.equals(this.type)){
			return new ApiMessageService();
		} else {
			return new LocalMessageService();
		}
	}

}
