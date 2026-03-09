package com.example.backendquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendQuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendQuizApplication.class, args);
    }

}
