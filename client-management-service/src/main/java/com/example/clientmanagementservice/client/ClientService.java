package com.example.clientmanagementservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public Client get(String clientId) {
        return clientRepository.findById(clientId)
//                .map(client)
                .orElseThrow(RuntimeException::new);
    }
}
