package com.example.clientmanagementservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

//    @Transactional
//    public ClientDto update(ClientDto client) {
//        log.info("Updating client....");
//        clientRepository.findById(client.getId())
//                .map((existingClientEntity) -> {
//                    log.info("Old client values: {}", existingClientEntity);
//                    log.info("New client values: {}", client);
//                    existingClientEntity.setName(client.getName().trim());
//                    existingClientEntity.setNameAr(client.getNameAr().trim());
//                    existingClientEntity.setDomain(client.getDomain().trim());
//                    existingClientEntity.setContactMobile(client.getContactMobile().trim());
//                    existingClientEntity.setContactPhone(client.getContactPhone().trim());
//                    existingClientEntity.setContactEmail(client.getContactEmail().trim());
//                    existingClientEntity.setFax(client.getFax().trim());
//                    existingClientEntity.setAddress(client.getAddress().trim());
//                    existingClientEntity.setActivity(activityMapper.toEntity(client.getActivity()));
//                    existingClientEntity.setEnabled(client.isEnabled());
//
////                    existingClient.setSubscription(client.getSubscription()); // This need to be thought about a client has many subscriptions but only one active at a time!
////                    if (!existingClient.getLogo().contains(client.getLogoFile().getName())) {
////                        //save the file
////                        existingClient.setLogo(existingClient.getId() + "/" + client.getLogoFile().getName());
////                    }
//
//                    existingClientEntity = clientRepository.save(existingClientEntity);
//                    log.info("Updating client done.");
//                    return existingClientEntity;
//                })
//                .map(clientMapper::toDto);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<ClientDto> getAll(Pageable pageable, String searchQuery) {
//        Page<ClientDto> clientPage;
//        if (!searchQuery.isEmpty()) {
//            log.info("Getting all clients with query: {}, and {} ....", searchQuery, pageable);
//            clientPage = clientRepository.findBySearchQuery(searchQuery, pageable).map(clientMapper::toDto);
//            log.info("Getting all clients found: {}.", clientPage.getTotalElements());
//        } else {
//            log.info("Getting all clients {} ....", pageable);
//            clientPage = clientRepository.findAll(pageable).map(clientMapper::toDto);
//            log.info("Getting all clients found: {}.", clientPage.getTotalElements());
//        }
//        return clientPage;
//    }
//
//    @Transactional(readOnly = true)
//    public ClientDto get(String id) {
//        log.info("Getting client by id: {} ....", id);
//        Optional<ClientDto> clientDto = clientRepository.findById(id).map(clientMapper::toDto);
//
//        if (clientDto.isPresent()) log.info("Getting client by id found: {}.", clientDto);
//        else log.error("Getting client by id: {} not found.", id);
//
//        return clientDto;
//    }
//
//    @Transactional(readOnly = true)
//    public ClientDto getByName(String name) {
//        log.info("Getting client by name: {} ....", name);
//        Optional<ClientDto> clientDto = clientRepository.findByNameIgnoreCase(name).map(clientMapper::toDto);
//
//        if (clientDto.isPresent()) log.info("Getting client by name found: {}.", clientDto);
//        else log.error("Getting client by name: {} not found.", name);
//
//        return clientDto;
//    }
//
//    @Transactional(readOnly = true)
//    public ClientDto getByNameAr(String nameAr) {
//        log.info("Getting client by nameAr: {} ....", nameAr);
//        Optional<ClientDto> clientDto = clientRepository.findByNameArIgnoreCase(nameAr).map(clientMapper::toDto);
//
//        if (clientDto.isPresent()) log.info("Getting client by nameAr found: {}.", clientDto);
//        else log.error("Getting client by nameAr: {} not found.", nameAr);
//
//        return clientDto;
//    }
//
//    @Transactional
//    public void delete(ClientDto client) {
//        log.info("Deleting client: {} ....", client);
//        Optional<ClientEntity> existingClient = clientRepository.findById(client.getId());
//
//        if (existingClient.isPresent()) {
//            existingClient.get().setDeleted(true);
//            clientRepository.save(existingClient.get());
//            log.info("Deleting client: {} done.", existingClient);
//
//        } else log.error("Deleting client: {} not found.", client);
//    }
}
