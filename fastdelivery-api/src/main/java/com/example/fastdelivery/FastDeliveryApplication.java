package com.example.fastdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableCassandraRepositories(basePackages = "com.example.fastdelivery.entities")
@SpringBootApplication
public class FastDeliveryApplication {



    public static void main(String[] args) {
        SpringApplication.run(FastDeliveryApplication.class, args);
    }

}
