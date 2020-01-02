package com.example.usermanagementservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") String userId){
        return userService.get(userId);
    }

    @GetMapping(params = {"page", "sort", "size"})
    public Page<UserDto> getUsers(Pageable pageable){
        return userService.getAll(pageable);
    }

    @GetMapping(params = "role")
    public List<UserDto> getUsersByRole(@RequestParam("role") String role){
        return userService.getAll(role);
    }
}
