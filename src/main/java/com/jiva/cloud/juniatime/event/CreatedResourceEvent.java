package com.jiva.cloud.juniatime.event;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Daniel Marques
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CreatedResourceEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private HttpServletResponse response;
  private String code;

  public CreatedResourceEvent(Object source, HttpServletResponse response, String code) {
    super(source);
    this.response = response;
    this.code = code;
  }
}
