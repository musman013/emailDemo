package com.fastcode.emaildemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.fastcode.emaildemo.domain.model.File;

@Configuration
public class SpringDataRestConfig {

    // Ensure that Spring Data ReST returns the Ids of created entities
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(File.class);
        });
    }
}
