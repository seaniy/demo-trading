package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            userService.createUser("admin");
        };
    }
}