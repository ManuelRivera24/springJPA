package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.OrderEntity;
import com.platzi.pizza.persistence.projection.OrderSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByDateAfter(LocalDateTime date);
    List<OrderEntity> findAllByMethodIn(List<String> methods); // Estamos usando "In" (al usar el keyword In debemos recibir una lista), para consultar los metodos

    // El metodo debe permitir consultar las ordenes que ha tenido un usuario en especifico dentro de nuestra pizzeria
    @Query(value = "select *from pizza_order where id_customer = :id", nativeQuery = true) // Aqui si debemos respetar los nombres que estan dentro de la base de datos (es una consulta normal, como la hacemos en una base de datos comunmente)
    List<OrderEntity> findCustomerOrders(@Param("id") String idCustomer); // No debemos respetar ningun nombramiento porque la consulta sera realizada por un Query nativo

    @Query(value = "select po.id_order as idOrder, cu.name as name, po.date as orderDate, po.total as orderTotal, group_concat(pi.name) as pizzaNames " +
            "from pizza_order po " +
            "inner join customer cu on po.id_customer = cu.id_customer " +
            "inner join order_item oi on po.id_order = oi.id_order " +
            "inner join pizza pi on oi.id_pizza = pi.id_pizza " +
            "where po.id_order = :orderId " +
            "group by po.id_order, cu.name, po.date, po.total", nativeQuery = true) // Importante: tener cuidado con los espacios, para que los tome como palabras separadas
    OrderSummary findSummary(@Param("orderId") int orderId);
}
