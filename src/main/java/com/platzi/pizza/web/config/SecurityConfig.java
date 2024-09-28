package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Aqui creamos las reglas de seguridad (personalizadas, porque ya se crean unas por defecto sin hacer esto)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Al autorizar las peticiones http, debe estar autenticado con http basic
        http
                .csrf().disable()
                .cors().and() // Esto para habilitar que la parte frondend pueda acceder a nuestra aplicacion back a traves de otro puerto (porque se trabajan con dos puertos diferentes, por ejemplo: en el backend por el 8080 y el fronend por el 4200 u otro)
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}
