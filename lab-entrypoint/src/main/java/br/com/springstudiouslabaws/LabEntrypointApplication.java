package br.com.springstudiouslabaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.com.springstudiouslabaws.labentrypoint",
        "br.com.springstudiouslabaws.labcore",
        "br.com.springstudiouslabaws.labdataprovider"
})
@EnableMongoRepositories(basePackages = "br.com.springstudiouslabaws.labdataprovider.config.mongodb")
public class LabEntrypointApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabEntrypointApplication.class, args);
    }

}
