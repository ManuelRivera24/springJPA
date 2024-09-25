package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends ListCrudRepository<PizzaEntity, Integer> { // Esta es una clase de spring data
    List<PizzaEntity> findAllByAvailableTrueOrderByPrice();
//    PizzaEntity findAllByAvailableTrueAndNameIgnoreCase(String name); // El "IgnoreCase" sirve para que no se tenga en cuenta mayusculas o minusculas al momento de buscar un nombre
    Optional <PizzaEntity> findFirstByAvailableTrueAndNameIgnoreCase(String name); // Aqui se cambio el "All", por el "First", para que no me traiga todos los registros (All), sino uno solo (first, el primero)
    List<PizzaEntity> findAllByAvailableTrueAndDescriptionContainingIgnoreCase(String description); // ContainingIgnoreCase nos ayudan a controlar las mayusculas y minusculas de nuestra consulta
    List<PizzaEntity> findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(String description); // En este caso se busca lo contrario al metodo de arriba (las pizzas que no lleven cierto ingrediente dentro de su descripcion)
    List<PizzaEntity> findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(double price); //En este caso usamos "Top3", para limitar las respuestas a tres (solo va a mostrar los primeros tres resultados, top es igual a first). "LessThanEqual" para decir que el precio sea menor o igual a un parametro que le pasemos
    int countByVeganTrue();
}
