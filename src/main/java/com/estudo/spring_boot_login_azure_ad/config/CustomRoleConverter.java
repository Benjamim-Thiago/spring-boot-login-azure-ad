package com.estudo.spring_boot_login_azure_ad.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extrai as roles do claim do token JWT (ajuste o claim conforme o necessário)
        List<String> roles = jwt.getClaimAsStringList("roles");

        // Converte as roles para o formato de GrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // Prefixa com 'ROLE_'
                .collect(Collectors.toList());
    }
}

