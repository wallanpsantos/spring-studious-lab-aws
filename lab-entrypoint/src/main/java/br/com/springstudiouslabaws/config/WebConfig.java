package br.com.springstudiouslabaws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                /// Padrão como JSON para todas as respostas da API
                .defaultContentType(MediaType.APPLICATION_JSON)
                /// Desativa a capacidade de especificar o tipo de mídia através de um parâmetro na URL
                .favorParameter(false)
                /// Responder no formato padrão (JSON), independentemente do que o cliente solicitar.
                .ignoreAcceptHeader(true)
                
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                .allowedHeaders("*");
    }
}