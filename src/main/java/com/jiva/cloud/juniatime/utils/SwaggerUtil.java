package com.jiva.cloud.juniatime.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;

public class SwaggerUtil {
  
  private SwaggerUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static final ResponseMessage M201 = recursoCriadoMessage();
  public static final ResponseMessage M204PUT = simpleMessage(204, "Atualização ok");
  public static final ResponseMessage M204DEL = simpleMessage(204, "Deleção ok");
  public static final ResponseMessage M403 = simpleMessage(403, "Não autorizado");
  public static final ResponseMessage M404 = simpleMessage(404, "Não encontrado");
  public static final ResponseMessage M422 = simpleMessage(422, "Erro de validação");
  public static final ResponseMessage M500 = simpleMessage(500, "Erro inesperado");

  private static ResponseMessage simpleMessage(int code, String msg) {
    return new ResponseMessageBuilder().code(code).message(msg).build();
  }

  private static ResponseMessage recursoCriadoMessage() {
    Map<String, Header> map = new HashMap<>();

    map.put("location", new Header("location", "URI do novo recurso", new ModelRef("string")));
    return new ResponseMessageBuilder().code(201).message("Recurso criado")
        .headersWithDescription(map).build();
  }

  public static List<ResponseMessage> getResponseMessage() {
    return Arrays.asList(M403, M404, M500);
  }

  public static List<ResponseMessage> postResponseMessage() {
    return Arrays.asList(M201, M403, M422, M500);
  }

  public static List<ResponseMessage> putResponseMessage() {
    return Arrays.asList(M204PUT, M403, M404, M422, M500);
  }

  public static List<ResponseMessage> deleteResponseMessage() {
    return Arrays.asList(M204DEL, M403, M404, M500);
  }

}
