package com.example.usermanagementservice.user;

import com.example.usermanagementservice.user.exception.ResourceNotFoundException;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createClient(@RequestBody UserDto user) throws URISyntaxException {
        log.info("requested to create a new user : {}", user);
        if (!(user.getId() == null || user.getId().trim().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client to be saved shouldn't have an id");
        }

        UserDto savedUser = userService.save(user);
        log.info("request to create a new user done.");
        return ResponseEntity.created(new URI("/api/v1/clients/" + savedUser.getId())).body(savedUser);
    }

    @PutMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateClient(@RequestBody UserDto user) {
        log.info("requested to update user : {}", user);
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client to be updated should have an id");
        }
        UserDto userDto;
        try {
            userDto = userService.update(user);

        } catch (ResourceNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id to be updated does not exist", exc);
        }

        log.info("request to update user done.");
        return ResponseEntity.ok(userDto);
    }

//    @GetMapping(value = "users/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<UserDto> getClient(@PathVariable("clientId") String clientId) {
//        log.info("requested to get client with id: {}.", clientId);
//        try {
//            log.info("requested to get client with id: {} done.", clientId);
//            return ResponseEntity.ok(userService.get(clientId));
//
//        } catch (ResourceNotFoundException exc) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id does not exist", exc);
//        }
//    }
//
//    @GetMapping(value = "clients", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Page<UserDto>> getAllClients(Pageable pageable, @RequestParam(value = "q", required = false) String searchQuery) {
//        log.info("requested to get all clients page: {} ...", pageable);
//        log.info("request to get all clients done.");
//        return ResponseEntity.ok(userService.getAll(pageable, searchQuery));
//    }
//
//    @DeleteMapping(value = "users/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") String clientId) {
//        log.info("requested to delete client id: {} ...", clientId);
//        try {
//            userService.delete(userService.get(clientId));
//            log.info("request to delete client done.");
//            return ResponseEntity.status(HttpStatus.OK).build();
//
//        } catch (ResourceNotFoundException ex) {
//            log.error("request to delete client (id:{}) not found.", clientId);
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id to be deleted does not exist", ex);
//        }
//    }
}
