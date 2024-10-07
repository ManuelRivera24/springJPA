package com.platzi.pizza.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Metodo donde se crea el filtro de seguridad (se valida que el jwt que llega con una peticion que requiere autenticacion es valido o no)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pasos para validar un JWT:
        // 1. Validar que sea un Header Autorization valido
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer")) { // Si cumple con estas condiciones no es una peticion valida
            filterChain.doFilter(request, response);
            return; // Este return es para que no continue hacia abajo (para cancelar la ejecucion del metodo)
        }

        // 2. Validar que el JWT sea valido
        String jwt = authHeader.split(" ")[1].trim(); // El espacio en blanco es por el espacio que hay despues de la palabra "Bearer"

        if(!this.jwtUtil.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cargar el usuario del UserDetailService
        String username = this.jwtUtil.getUsername(jwt);
        User user = (User) this.userDetailsService.loadUserByUsername(username); // El metodo "loadUserByUsername" ejecuta el meodo que esta dentro de la clase "UserSecurityService", buscaria el usuario dentro de la base de datos y lo retornara a nuestro filter

        // 4. Cargar al usuario en el contexto de seguridad
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities()
        ); // El constructor de la clase "UsernamePasswordAuthenticationToken" recibe estos tres parametros, y dentro del mismo metodo se pone "autenticado"

        // Incluimos los detalles
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Cargamos la autenticacion al contexto de seguridad
        System.out.println(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
