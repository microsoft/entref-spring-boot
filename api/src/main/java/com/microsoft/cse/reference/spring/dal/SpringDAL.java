package com.microsoft.cse.reference.spring.dal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableMongoRepositories
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@EnableResourceServer
public class SpringDAL {
    /**
     * Application entry point. Scans for spring beans and automatically loads them
     * @param args passed arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringDAL.class, args);
    }
}
