package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MongoTestContainerConfiguration {

    @Bean
    MongoDBContainer mongoDBContainer() {

        MongoDBContainer container =
                new MongoDBContainer("mongo:7.0");

        container.start();

        return container;
    }

    @Bean
    MongoClient mongoClient(
            MongoDBContainer container) {

        return MongoClients.create(
                container.getConnectionString()
        );
    }
}