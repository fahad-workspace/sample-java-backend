package com.sample.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public static OpenAPI movieDatabaseOpenAPI() {
    Server localServer =
        new Server().url("http://localhost:8080").description("Development server");
    Contact contact =
        new Contact()
            .name("API Support Team")
            .email("support@moviedb.example.com")
            .url("https://www.example.com/support");
    License mitLicense =
        new License().name("MIT License").url("https://opensource.org/licenses/MIT");
    Info info =
        new Info()
            .title("Movie Database API")
            .description("REST API for managing movies, actors, directors, and their relationships")
            .version("1.0.0")
            .contact(contact)
            .license(mitLicense)
            .termsOfService("https://www.example.com/terms");
    return new OpenAPI().info(info).servers(List.of(localServer));
  }
}
