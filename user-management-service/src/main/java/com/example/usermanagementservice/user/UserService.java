package com.example.usermanagementservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User get(String userId) {
        return userRepository.findById(userId)
//                .map() TODO return a mapped DTO
                .orElseThrow(RuntimeException::new);
    }
}
