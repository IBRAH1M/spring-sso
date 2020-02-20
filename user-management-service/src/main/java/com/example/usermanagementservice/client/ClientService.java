package com.example.usermanagementservice.client;

import com.example.usermanagementservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientManagementServiceClient clientManagementServiceClient;

    public Client getClient(String clientId) {
        log.debug("Calling client management service to get client with id: {}.....", clientId);
        final Client client = Optional.ofNullable(clientManagementServiceClient.getClientById(clientId))
                .orElseThrow(ResourceNotFoundException::new);
        log.debug("Calling client management service done. client: {}.", client);
        return client;
    }

    public int getServiceHealth() {
        String status = clientManagementServiceClient.getServiceHealth();
        log.debug("Client Management Service Health: {}.", status);
        return status.contains("UP") ? 1 : 0;
    }
}
