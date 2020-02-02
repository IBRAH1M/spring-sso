package com.example.usermanagementservice.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@EnableCircuitBreaker
@EnableFeignClients
@Configuration
public class ClientManagementServiceClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return (String methodKey, Response response) -> {
            switch (response.status()) {
                case 400:
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST);
                case 404:
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                default:
                    return new Exception("Generic error");
            }
        };
    }
}
