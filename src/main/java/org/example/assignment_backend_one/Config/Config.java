package org.example.assignment_backend_one.Config;

import org.example.assignment_backend_one.Models.AppUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public AppUser appUser(){
        return new AppUser();
    }
}
