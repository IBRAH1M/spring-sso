package com.example.authorizationserver.user;

import com.example.authorizationserver.security.CurrentUser;
import com.example.authorizationserver.security.LoggedInUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public CurrentUser getUser(@LoggedInUser CurrentUser currentUser) {
        return currentUser;
    }

    @GetMapping("/{userId}")
    public CurrentUser getUser(@LoggedInUser CurrentUser currentUser, @PathVariable String userId) {
        return currentUser;
    }
}



