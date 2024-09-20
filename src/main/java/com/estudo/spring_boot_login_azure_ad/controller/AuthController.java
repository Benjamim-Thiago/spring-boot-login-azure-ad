package com.estudo.spring_boot_login_azure_ad.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {
    @Value("${azure.activedirectory.client-id}")
    private String clientId;

    @Value("${azure.activedirectory.client-secret}")
    private String clientSecret;

    @Value("${azure.activedirectory.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.azure.token-uri}")
    private String tokenUrl;

    @GetMapping("/login/oauth2/code/azure")
    public String getJwtToken(@AuthenticationPrincipal OidcUser principal) {
        System.out.println(principal.getIdToken().getTokenValue());
        return "Token JWT: " + principal.getIdToken().getTokenValue();
    }

    @GetMapping("/login-redirect")
    public ResponseEntity<String> loginRedirect(@RequestParam("code") String code) {
        // Chamar método para trocar o código pelo token
        String accessToken = exchangeCodeForToken(code);
        if (accessToken != null) {
            return ResponseEntity.ok("Token JWT: " + accessToken);
        } else {
            return ResponseEntity.badRequest().body("Falha ao obter o token");
        }
    }

    private String exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Configurar headers para a requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Montar o corpo da requisição
        String requestBody = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Fazer a chamada ao endpoint de token do Azure
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

        // Retornar o token
        System.out.println(response.getBody());
        return response.getBody(); // Aqui você pode extrair o token do corpo da resposta se necessário
    }
}

