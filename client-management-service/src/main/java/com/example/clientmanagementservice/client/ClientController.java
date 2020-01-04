package com.example.clientmanagementservice.client;

import com.example.clientmanagementservice.client.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1/")
@RestController
public class ClientController {

    private final ClientService clientService;

    @PostMapping(value = "clients/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto client) throws URISyntaxException {
        log.info("requested to create a new client : {}", client);
        if (!(client.getId() == null || client.getId().trim().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client to be saved shouldn't have an id");
        }

        ClientDto savedClient = clientService.save(client);
        log.info("request to create a new client done.");
        return ResponseEntity.created(new URI("/api/v1/clients/" + savedClient.getId())).body(savedClient);
    }

    @PutMapping(value = "clients/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto client) {
        log.info("requested to update client : {}", client);
        if (client.getId() == null || client.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client to be updated should have an id");
        }

        try {
            clientService.get(client.getId());

        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id to be updated does not exist", exc);
        }

        log.info("request to update client done.");
        return ResponseEntity.ok(clientService.update(client));
    }

    @GetMapping(value = "clients/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getClient(@PathVariable("clientId") String clientId) {
        log.info("requested to get client with id: {}.", clientId);
        try {
            log.info("requested to get client with id: {} done.", clientId);
            return ResponseEntity.ok(clientService.get(clientId));

        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id does not exist", exc);
        }
    }

    @GetMapping(value = "clients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ClientDto>> getAllClients(Pageable pageable, @RequestParam(value = "q", required = false) String searchQuery) {
        log.info("requested to get all clients page: {} ...", pageable);
        log.info("request to get all clients done.");
        return ResponseEntity.ok(clientService.getAll(pageable, searchQuery));
    }

    @DeleteMapping(value = "clients/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") String clientId) {
        log.info("requested to delete client id: {} ...", clientId);
        try {
            clientService.delete(clientService.get(clientId));
            log.info("request to delete client done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException ex) {
            log.error("request to delete client (id:{}) not found.", clientId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id to be deleted does not exist", ex);
        }
    }
}
