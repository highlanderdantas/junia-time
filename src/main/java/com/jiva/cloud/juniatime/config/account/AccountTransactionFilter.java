package com.jiva.cloud.juniatime.config.account;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import com.jiva.cloud.juniatime.config.property.JuniaTimeProperty;

import lombok.Getter;

import static com.jiva.cloud.juniatime.config.account.AccountTransactionHandler.*;

@Component
public class AccountTransactionFilter implements Filter {

  @Getter
  private String urlAccount;
  
  AccountTransactionFilter(JuniaTimeProperty property) {
    this.urlAccount = property.getUrlAccount();
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    if ("OPTIONS".equals(request.getMethod())) {
      response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST , GET , DELETE, PUT, OPTIONS");
      response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type, Accept");
      response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
      response.setStatus(HttpServletResponse.SC_OK);

    } else {
        if (!skipFilter(request.getRequestURI()) && !callAccount(request, getUrlAccount())) {
          buildMessageError(response);
        }
        chain.doFilter(req, res);
     }

  }
}
