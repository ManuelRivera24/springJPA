package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends ListCrudRepository<CustomerEntity, String> { // El id de "CustomerEntity" es un String

    // Aqui se usan entitys en lugar de tablas (estas consultas son muy parecidos a los SQL nativos)
    @Query(value = "select c from CustomerEntity c where c.phoneNumber = :phone") // Aca ya no se va a usar querymethods, sino que utilizaremos la anotacion @Query (":phone" significa que voy a tener un parametro que tiene este nombre)
    CustomerEntity findByPhone(@Param("phone") String phone); // Vamos a recibir como parametro un "phone"
}
