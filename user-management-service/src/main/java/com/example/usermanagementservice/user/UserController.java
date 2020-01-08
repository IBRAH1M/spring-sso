package com.example.usermanagementservice.user;

import com.example.usermanagementservice.user.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody UserDto user) throws URISyntaxException {
        log.info("requested to create a new user : {}", user);
        if (!(user.getId() == null || user.getId().trim().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to be saved shouldn't have an id");
        }

        UserDto savedUser = userService.save(user);
        log.info("request to create a new user done.");
        return ResponseEntity.created(new URI("/api/v1/users/" + savedUser.getId())).body(savedUser);
    }

    @PutMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@Validated @RequestBody UserDto user) {
        log.info("requested to update user : {}", user);
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to be updated should have an id");
        }

        try {
            UserDto userDto = userService.update(user);
            log.info("request to update user done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id to be updated does not exist", exc);
        }
    }

    @GetMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
        log.info("requested to get user with id: {}.", userId);
        try {
            log.info("requested to get user with id: {} done.", userId);
            return ResponseEntity.ok(userService.get(userId));

        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id does not exist", exc);
        }
    }

    @GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable, @RequestParam(value = "q", required = false, defaultValue = "") String searchQuery) {
        log.info("requested to get all users page: {} ...", pageable);
        Page<UserDto> userDtos = userService.getAll(pageable, searchQuery);
        log.info("request to get all users done.");
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        log.info("requested to delete user id: {} ...", userId);
        try {
            userService.delete(userService.get(userId));
            log.info("request to delete user done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException ex) {
            log.error("request to delete user (id:{}) not found.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id to be deleted does not exist", ex);
        }
    }
}