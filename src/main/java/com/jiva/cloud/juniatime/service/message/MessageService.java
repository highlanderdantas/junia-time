package com.jiva.cloud.juniatime.service.message;

import com.jiva.cloud.juniatime.event.StatusCheckEvent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface MessageService {

	void send(StatusCheckEvent event);

	default String buildPayload(Message message) {

		return new StringBuilder()
					.append("{\"text\": ")
					.append("\"")
					.append("<http://")
					.append(message.getUrl())
					.append("|")
					.append(message.getClient())
					.append(" - ")
					.append(message.getProduct())
					.append("> is ")
					.append(message.getStatus())
					.append("!")
					.append(" \", \"icon_emoji\": \":fire:\"")
					.append(", \"username\":\"junia-time\"}")
				.toString();
	}
	
	default Message buildMessage(StatusCheckEvent event, String url) {
		var check = event.getCheck();

		return Message.builder()
				.client(check.getClient().getName())
				.product(check.getProduct().getName())
				.status(event.getStatus().getDescricao().toLowerCase())
				.url(url)
				.build();
	}
	
	
	@Getter
	@Setter
	@Builder
	class Message {
		private String client;
		private String status;
		private String product;
		private String url;
	}

}
