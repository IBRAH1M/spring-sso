package com.example.clientmanagementservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1/clients")
@RestController
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{clientId}")
    public Client getClient(@PathVariable("clientId") String clientId) {
        return clientService.get(clientId);
    }
}

