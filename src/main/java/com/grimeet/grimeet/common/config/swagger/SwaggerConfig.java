package com.grimeet.grimeet.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
            .info(new Info().title("Grimeet API")
                    .description("그림으로 연결되는 우리 그리밋(Grim:eet) API")
                    .version("v0.0.1")
                    .contact(new Contact()
                            .name("Grimeet")
                            .email("jgoneit@gmail.com")
                    )
            ).servers(List.of(new Server().url("http://localhost:8081").description("Local Server"),
                    new Server().url("https://api.grimeet.com").description("Production Server"))
            ).components(new Components().addSecuritySchemes("bearer-jwt",
                    new SecurityScheme().type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .in(SecurityScheme.In.COOKIE)
                            .name("Authorization_Access"))
            ).addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
  }
}
