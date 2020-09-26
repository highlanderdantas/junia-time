package com.jiva.cloud.juniatime.dto;

import java.util.HashMap;

public class GenericResponse extends HashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  public static GenericResponse build(String key, Object value) {
    GenericResponse response = new GenericResponse();
    response.put(key, value);
    return response;
  }
}
