package com.jiva.cloud.juniatime.event.listener;

import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.jiva.cloud.juniatime.event.CreatedResourceEvent;

@Component
public class CreatedResourceListener implements ApplicationListener<CreatedResourceEvent> {

  @Override
  public void onApplicationEvent(CreatedResourceEvent event) {
    HttpServletResponse response = event.getResponse();
    String code = event.getCode();

    addHeaderLocation(response, code);
  }

  private void addHeaderLocation(HttpServletResponse response, String code) {
    URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{check}")
        .buildAndExpand(code).toUri();
    response.setHeader("Location", uri.toASCIIString());
  }

}
