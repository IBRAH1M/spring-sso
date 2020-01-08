package com.example.clientmanagementservice.client;

import com.example.clientmanagementservice.client.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientDto save(ClientDto clientDto) {
        log.info("Saving client....");
        ClientEntity existingClientEntity = clientRepository.save(clientMapper.toEntity(clientDto));
        log.info("Saving client done. Client id: {}", existingClientEntity.getId());
        return clientMapper.toDto(existingClientEntity);
    }

    @Transactional
    public ClientDto update(ClientDto client) {
        log.info("Updating client....");
        return clientRepository.findById(client.getId())
                .map((existingClientEntity) -> {
                    log.info("Old client values: {}", existingClientEntity);
                    log.info("New client values: {}", client);
                    existingClientEntity.setName(client.getName().trim());
                    existingClientEntity.setNameAr(client.getNameAr().trim());
                    existingClientEntity = clientRepository.save(existingClientEntity);
                    log.info("Updating client done.");
                    return existingClientEntity;
                })
                .map(clientMapper::toDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> getAll(Pageable pageable, String searchQuery) {
        Page<ClientDto> clientPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all clients filtered by query: {}, and page: {} ....", searchQuery, pageable);
            clientPage = clientRepository.findBySearchQuery(pageable, searchQuery).map(clientMapper::toDto);
        } else {
            log.info("Getting all clients {} ....", pageable);
            clientPage = clientRepository.findAll(pageable).map(clientMapper::toDto);
        }
        log.info("Getting all clients done found: {}.", clientPage.getTotalElements());
        return clientPage;
    }

    @Transactional(readOnly = true)
    public ClientDto get(String id) {
        log.info("Getting client by id: {} ....", id);
        return clientRepository.findById(id)
                .map(clientEntity -> {
                    log.info("Getting client by id found: {}.", clientEntity);
                    return clientEntity;
                })
                .map(clientMapper::toDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(ClientDto client) {
        log.info("Deleting client: {} ....", client);
        clientRepository.findById(client.getId())
                .map(existingClient -> {
                    clientRepository.delete(existingClient);
                    log.info("Deleting client: {} done.", existingClient);
                    return existingClient;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
