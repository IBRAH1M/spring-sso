package com.example.clientmanagementservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class ClientManagementServiceConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("NO SECURITY USER YET");
//        return () -> Optional.of(((Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
