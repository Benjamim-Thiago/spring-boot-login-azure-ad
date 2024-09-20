package com.estudo.spring_boot_login_azure_ad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.client.registration.azure.jwk-set-uri}")
    private String jwkSetUri = "";

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/home").hasRole("VER_HOME")
//                                .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .defaultSuccessUrl("/home", true)
//                )
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/logout")
//                                .logoutSuccessUrl("/")
//                )
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwt ->
//                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) // Configura o converter de roles JWT
//                        )
//                );
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/items").authenticated() // Protege o endpoint /items
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/azure") // URL de login do Azure
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redireciona após logout
                );
        return http.build();
    }

    // Configura o JwtAuthenticationConverter para mapear roles
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomRoleConverter()); // Converter customizado para roles
        return converter;
    }

    // Configuração do JwtDecoder usando NimbusJwtDecoder
    @Bean
    public JwtDecoder jwtDecoder() {
        // Substitua YOUR_TENANT_ID pela identificação correta do tenant no Azure AD
        
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
}
