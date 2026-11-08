package br.com.springstudiouslabaws.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de empréstimos e pagamentos")
                        .version("1.0.0")
                        .description("""
                                Contrato HTTP do laboratório. Erros usam um único envelope:
                                error.code, error.message e error.details.
                                POST sem Idempotency-Key não é seguro para retentativa.
                                """))
                .servers(List.of(new Server().url("http://localhost:8082").description("Local")));
    }
}
