package com.estudo.spring_boot_login_azure_ad.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    //@Value("${azure.activedirectory.redirect-uri}")
    @Value("${spring.security.oauth2.client.registration.azure.redirect-uri}")
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
            System.out.println(accessToken);
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
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);

        // Criar a entidade da requisição com headers e body
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Fazer a chamada ao endpoint de token do Azure
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
            // Se a requisição for bem-sucedida, retornamos o corpo da resposta (o token JWT)
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

