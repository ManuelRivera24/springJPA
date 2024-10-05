package com.platzi.pizza.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private static String SECRET_KEY = "pl4tz1_p1zz4"; // Para poner como parametri en el Algorithm
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY); // Para poner como parametro dentro del "sign()"

    public String create(String username) {
        return JWT.create()
                .withSubject(username) // El asunto siempre sera el usuario en cuestion
                .withIssuer("platzi-pizza") // Quien crea este JWT
                .withIssuedAt(new Date()) // La fecha en la que fue creado este token
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15))) // Hasta cuando es valido este token (cuando expira, en este caso expira en 15 dias a partir de la fecha actual)
                .sign(ALGORITHM); // Firmar nuestro token
    }

    // Este metodo valida que el jwt sea valido, si es valido retorna un true, y sino retorna un false
    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
