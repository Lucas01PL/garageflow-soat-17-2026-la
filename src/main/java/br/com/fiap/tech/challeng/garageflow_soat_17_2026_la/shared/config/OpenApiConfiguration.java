package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI garageFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Garage Flow API")
                        .description("API for Garage Flow Management System")
                        .version("v0.0.1-SNAPSHOT"))
                .addServersItem(new Server()
                        .url("http://localhost:8080/api")
                        .description("Local Environment"));
    }

}