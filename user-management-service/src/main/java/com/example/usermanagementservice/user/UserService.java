package com.example.usermanagementservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto get(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDro)
                .orElseThrow(RuntimeException::new);
    }

    public Page<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDro);
    }

    public List<UserDto> getAll(String role) {
        return null;
    }
}
