package com.example.usermanagementservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientManagementServiceHealth implements HealthIndicator {

    private final ClientService clientService;

    @Override
    public Health health() {
        int status = clientService.getServiceHealth();
        if (status == 0) {
            return Health.down().withDetail("Downstream service: Client Management is not healthy", status).build();
        }
        return Health.up().build();
    }
}
