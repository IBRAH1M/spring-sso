package com.example.usermanagementservice.user;

import com.example.usermanagementservice.user.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto save(UserDto clientDto) {
        log.info("Saving client....");
        UserEntity existingUserEntity = userRepository.save(userMapper.toEntity(clientDto));
        log.info("Saving client done. Client id: {}", existingUserEntity.getId());
        return userMapper.toDto(existingUserEntity);
    }

    @Transactional
    public UserDto update(UserDto user) {
        log.info("Updating user....");
        return userRepository.findById(user.getId())
                .map((existingUserEntity) -> {
                    log.info("Old user values: {}", existingUserEntity);
                    log.info("New user values: {}", user);
                    existingUserEntity.setName(user.getName().trim());
                    existingUserEntity.setNameAr(user.getNameAr().trim());
                    existingUserEntity = userRepository.save(existingUserEntity);
                    log.info("Updating user done.");
                    return existingUserEntity;
                })
                .map(userMapper::toDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAll(Pageable pageable, String searchQuery) {
        Page<UserDto> clientPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all clients filtered by query: {}, and page: {} ....", searchQuery, pageable);
            clientPage = userRepository.findBySearchQuery(searchQuery, pageable).map(userMapper::toDto);
            log.info("Getting all clients found: {}.", clientPage.getTotalElements());
        } else {
            log.info("Getting all clients {} ....", pageable);
            clientPage = userRepository.findAll(pageable).map(userMapper::toDto);
            log.info("Getting all clients found: {}.", clientPage.getTotalElements());
        }
        return clientPage;
    }

//    @Transactional(readOnly = true)
//    public UserDto get(String id) {
//        log.info("Getting client by id: {} ....", id);
//        Optional<UserDto> clientDto = userRepository.findById(id).map(userMapper::toDto);
//
//        if (clientDto.isPresent()) log.info("Getting client by id found: {}.", clientDto);
//        else log.error("Getting client by id: {} not found.", id);
//
//        return clientDto;
//    }

//    @Transactional
//    public void delete(UserDto client) {
//        log.info("Deleting client: {} ....", client);
//        Optional<UserEntity> existingClient = userRepository.findById(client.getId());
//
//        if (existingClient.isPresent()) {
//            existingClient.get().setDeleted(true);
//            userRepository.save(existingClient.get());
//            log.info("Deleting client: {} done.", existingClient);
//
//        } else log.error("Deleting client: {} not found.", client);
//    }
}
