package com.jiva.cloud.juniatime.config.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.jiva.cloud.juniatime.dto.GenericResponse;

public class AccountTransactionHandler {

  protected static void buildMessageError(HttpServletResponse response) throws IOException {
    String messageJson = new Gson()
        .toJson(GenericResponse.build("Error", "Falha na autenticacao, verifique seu token!!"));

    ServletOutputStream out = response.getOutputStream();
    response.setStatus(401);

    out.print(messageJson);
    out.flush();
    out.close();
  }

  public static Boolean callAccount(HttpServletRequest request, String urlAccount) throws IOException {

    String token = request.getHeader("Authorization");
    if (token == null)
      return false;

    Map<String, String> params = new HashMap<String, String>();
    params.put("token", token.replace("Bearer ", ""));
    HttpStatus codeResponse = null;

    try {
      codeResponse = new RestTemplate()
          .postForEntity(urlAccount, params, String.class).getStatusCode();
    } catch (HttpClientErrorException e) {
      return false;
    }
    return HttpStatus.OK.equals(codeResponse);

  }
  
  public static Boolean skipFilter(String url) {
    List<String> urlToSkip = new ArrayList<>();
    urlToSkip.add("swagger");
    urlToSkip.add("api-docs");
    urlToSkip.add("/csrf");

    return urlToSkip.stream()
        .filter(u -> url.indexOf(u) >= 0 || url.length() < 2)
        .findFirst()
        .isPresent();
  }

}
