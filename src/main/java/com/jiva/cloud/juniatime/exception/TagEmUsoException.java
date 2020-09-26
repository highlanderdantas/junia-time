package com.jiva.cloud.juniatime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class TagEmUsoException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TagEmUsoException() {
    super("Tag em Uso, remova os checks que estão vinculados a ela para depois removê-la");
  }
}
