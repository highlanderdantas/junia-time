package com.jiva.cloud.juniatime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PingCountInexistenteOuInvalidoException extends RuntimeException {

  private static final String PING_COUNT_NAO_ENCONTRADO_OU_INVALIDO = "PingCount não encontrado ou inválido.";
  private static final long serialVersionUID = 1L;

  public PingCountInexistenteOuInvalidoException() {
    super(PING_COUNT_NAO_ENCONTRADO_OU_INVALIDO);
  }
  
  public PingCountInexistenteOuInvalidoException(String tagDescription) {
    super(PING_COUNT_NAO_ENCONTRADO_OU_INVALIDO + ": " + tagDescription);
  }
  
  
}
