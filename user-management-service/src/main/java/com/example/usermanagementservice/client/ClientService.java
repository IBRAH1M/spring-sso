package com.example.usermanagementservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientManagementServiceClient clientManagementServiceClient;

    public Client getClient(String clientId) {
        return clientManagementServiceClient.getClientById(clientId);
    }
}
