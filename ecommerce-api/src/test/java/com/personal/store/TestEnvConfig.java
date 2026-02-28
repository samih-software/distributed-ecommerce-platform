package com.personal.store;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class TestEnvConfig {

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void loadTestEnv() {
        String activeProfile = environment.getActiveProfiles().length > 0
                ? environment.getActiveProfiles()[0]
                : "";

        if ("test".equals(activeProfile)) {

            Dotenv dotenv = Dotenv.configure()
                    .filename(".env.test")
                    .ignoreIfMissing()
                    .load();

            // Map env vars to Spring properties
            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );
        }
    }
}