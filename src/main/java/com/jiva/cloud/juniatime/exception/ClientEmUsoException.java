package com.jiva.cloud.juniatime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ClientEmUsoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ClientEmUsoException() {
    super("Client em uso, remova os checks que estão vinculados a ele para depois removê-lo");
  }
}
