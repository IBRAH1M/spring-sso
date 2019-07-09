package com.example.assetservice.asset;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @GetMapping
    public Principal getAsset(Principal principal) {
        return principal;
    }
}
