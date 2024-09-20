package com.estudo.spring_boot_login_azure_ad.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.stream.Collectors;

@RestController
public class HomeController {

    @GetMapping("/home")
   // @PreAuthorize("hasRole('VER_HOME')")
    public String home(@AuthenticationPrincipal OidcUser user) {
        // Extrair e listar as roles (GrantedAuthorities) do usuário
        String roles = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(", "));

        return "Bem-vindo ao Home, " + user.getFullName() + "! Suas roles são: " + roles;
    }
}
