package com.platzi.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories // De esta manera le indicamos a nuestra aplicacion de spring que utilizaremos repositorios de spring
public class PlatziPizzeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatziPizzeriaApplication.class, args);
	}

}
