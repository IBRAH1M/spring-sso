package com.example.usermanagementservice.user;

import com.example.usermanagementservice.client.Client;
import com.example.usermanagementservice.client.ClientService;
import com.example.usermanagementservice.exception.ResourceNotFoundException;
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
    private final ClientService clientService;

    @Transactional
    public UserDto save(UserDto userDto) {
        log.info("Saving user....");
        try {
            clientService.getClient(userDto.getClientId());
        } catch (ResourceNotFoundException ex) {
            log.error("Client with id:{}. Not found.", userDto.getClientId());
            ex.printStackTrace();
            throw ex;
        }
        UserEntity existingUserEntity = userRepository.save(userMapper.toEntity(userDto));
        log.info("Saving user done. User id: {}", existingUserEntity.getId());
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
        Page<UserDto> userPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all users filtered by query: {}, and page: {} ....", searchQuery, pageable);
            userPage = userRepository.findBySearchQuery(pageable, searchQuery).map(userMapper::toDto);
        } else {
            log.info("Getting all users {} ....", pageable);
            userPage = userRepository.findAll(pageable).map(userMapper::toDto);
        }
        log.info("Getting all users done found: {}.", userPage.getTotalElements());
        return userPage;
    }

    @Transactional(readOnly = true)
    public UserDto get(String id) {
        log.info("Getting user by id: {} ....", id);
        return userRepository.findById(id)
                .map(userEntity -> {
                    log.info("Getting user by id found: {}.", userEntity);
                    return userEntity;
                })
                .map(userMapper::toDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(UserDto user) {
        log.info("Deleting user: {} ....", user);
        userRepository.findById(user.getId())
                .map(existingUser -> {
                    userRepository.delete(existingUser);
                    log.info("Deleting user: {} done.", existingUser);
                    return existingUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
