package com.estudo.spring_boot_login_azure_ad.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ItemsController {

    @GetMapping("/items")
    @PreAuthorize("hasRole('VER_HOME')")
    public List<String> listItems() {
        return Arrays.asList("Item 1", "Item 2", "Item 3");
    }
}