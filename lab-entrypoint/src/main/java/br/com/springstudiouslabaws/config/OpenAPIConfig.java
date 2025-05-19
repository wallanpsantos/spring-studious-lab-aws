package br.com.springstudiouslabaws.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server()
                .url("http://localhost:8082")
                .description("Ambiente de Desenvolvimento");

        Server qaServer = new Server()
                .url("https://api-qa.exemplo.com")
                .description("Ambiente de QA");

        Server prodServer = new Server()
                .url("https://api.exemplo.com")
                .description("Ambiente de Produção");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT token de autenticação");

        return new OpenAPI()
                .info(new Info()
                        .title("API de Processamento de Empréstimos e Pagamentos")
                        .version("1.0.0")
                        .description("API para gerenciamento de empréstimos e processamento de pagamentos com integração AWS")
                        .termsOfService("https://www.exemplo.com/terms")
                        .contact(new Contact()
                                .name("Time de Desenvolvimento")
                                .email("dev@empresa.com")
                                .url("https://www.exemplo.com/team"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(devServer, qaServer, prodServer))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", securityScheme))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}