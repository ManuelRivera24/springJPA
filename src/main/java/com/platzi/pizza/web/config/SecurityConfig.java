package com.platzi.pizza.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true) // Tenemos que habilitarla para que spring pueda controlar este tipo de anotaciones que estan por fuera del controlador
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Aqui creamos las reglas de seguridad (personalizadas, porque ya se crean unas por defecto sin hacer esto)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Este metodo se ejecuta de manera escalonada, por lo que si importa el orden en el que coloquemos cada instruccion
        // Al autorizar las peticiones http, debe estar autenticado con http basic
        http
                .csrf().disable()
                .cors().and() // Esto para habilitar que la parte frondend pueda acceder a nuestra aplicacion back a traves de otro puerto (porque se trabajan con dos puertos diferentes, por ejemplo: en el backend por el 8080 y el fronend por el 4200 u otro)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // Aqui estamos diciendo que nuestra aplicacion sera una api stateless
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER") // Va a permitir hacer un GET a Pizzas solo a los que tengan los roles que pasamos por parametros (en este caso por un admin y por un customer)
                .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN") // Se indica que rol tiene permitido hacer un POST
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN") // Se indica que solo los admin pueden hacer un PUT
                .requestMatchers("/api/orders/random").hasAuthority("random_order") // Aqui no se le asigna un rol sino que se le da una autorizacion para que pueda hacer cierta peticion (en este caso se le da una autorizacion al cliente para quer haga una peticion por medio del path random)
                .requestMatchers("/api/orders/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // La proteccion estara establecida por jwtfilter
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Es ta basado en el algoritmo BCrypt
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception { // Este metodo se crea para que lo padamos inyectar en el controlador
        return configuration.getAuthenticationManager();
    }
}
