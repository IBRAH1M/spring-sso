package com.example.authenticationauthorizationserver.authenticateduser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthenticatedUserController {

    @GetMapping("/me")
    public AuthenticatedUser getUser(@LoggedInUser AuthenticatedUser currentUser) {
        return currentUser;
    }
}
