package com.example.usermanagementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ClientManagementServiceClient",
        url = "${application.client-management-service-url}",
        fallback = ClientManagementServiceClient.ClientManagementServiceClientFallback.class)
interface ClientManagementServiceClient {

    @GetMapping(value = "api/v1/clients/{clientId}", produces = "application/json")
    Client getClientById(@PathVariable("clientId") String clientId);

    @Component
    class ClientManagementServiceClientFallback implements ClientManagementServiceClient {

        @Override
        public Client getClientById(String clientId) {
            return null;
        }
    }
}
