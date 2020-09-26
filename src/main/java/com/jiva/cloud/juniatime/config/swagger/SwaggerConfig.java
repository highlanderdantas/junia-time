package com.jiva.cloud.juniatime.config.swagger;


import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jiva.cloud.juniatime.utils.SwaggerUtil;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)

        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET, SwaggerUtil.getResponseMessage())
        .globalResponseMessage(RequestMethod.POST, SwaggerUtil.postResponseMessage())
        .globalResponseMessage(RequestMethod.PUT, SwaggerUtil.putResponseMessage())
        .globalResponseMessage(RequestMethod.DELETE, SwaggerUtil.deleteResponseMessage()).select()
        .apis(RequestHandlerSelectors.basePackage("com.jiva.cloud.juniatime.resource"))
        .paths(PathSelectors.any()).build().apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo("API Junia Time",
        "Esta API é utilizada para verificar os status dos sites da Jiva Cloud.", "Versão 1.0",
        "https://www.app.moove.com.br",
        new Contact("Daniel Marques", "", "daniel.masilva@hotmail.com"), null, null,
        Collections.emptyList());
  }

}
