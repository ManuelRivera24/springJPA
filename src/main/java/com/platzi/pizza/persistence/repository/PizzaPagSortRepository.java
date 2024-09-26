package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface PizzaPagSortRepository extends ListPagingAndSortingRepository<PizzaEntity, Integer> { // Sirve para paginar y ordenar
    // Aqui ya no se va a retornar una lista, ni un elemento sino una pagina de nuestra entidad (en este caso de PizzaEntity)
    Page<PizzaEntity> findByAvailableTrue(Pageable pageable); // Importante: Todo lo que retorne una pagina debe recibir un Pageable
}
