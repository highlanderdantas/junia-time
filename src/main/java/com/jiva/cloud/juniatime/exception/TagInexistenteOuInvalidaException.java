package com.jiva.cloud.juniatime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TagInexistenteOuInvalidaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TagInexistenteOuInvalidaException() {
    super("Tag inexistente ou invalida");
  }
}
