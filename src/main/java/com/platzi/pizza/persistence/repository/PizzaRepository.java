package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

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
    // Este metodo va a permitir actualizar el precio de una pizza directamente desde un SQL nativo
    // Aqui se utiliza otra manera, pero es lo mismo que hacerlo asi: @Param("idPizza") int idPizza, @Param("newPrice") double newPrice
    @Query(value = "update pizza " +
            "set price = :#{#newPizzaPrice.newPrice} " + // Toca respetar esto: "#{# }" para que funcione de esta manera (y escribir el nombre de la variable como este en la otra clase, en este caso la clase llamada: UpdatePizzaPriceDto)
            "where id_pizza = :#{#newPizzaPrice.pizzaId}", nativeQuery = true)
    @Modifying // Es importante usar esta anotacione en los @Query (cuando se vayan a hacer instrucciones como insert, delete o update)
    void updatePrice(@Param("newPizzaPrice")UpdatePizzaPriceDto newPizzaPrice);
}
