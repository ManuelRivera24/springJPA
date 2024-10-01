package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER") // Va a permitir hacer un GET a Pizzas solo a los que tengan los roles que pasamos por parametros (en este caso por un admin y por un customer)
                .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN") // Se indica que rol tiene permitido hacer un POST
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN") // Se indica que solo los admin pueden hacer un PUT
                .requestMatchers("/api/orders/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Es ta basado en el algoritmo BCrypt
    }
}
