package com.platzi.pizza.persistence.projection;

import java.time.LocalDateTime;

public interface OrderSummary {
    // En esta clase van los atributos de nuestro Query personalizado
    // Una interfaz siempre debe expresarse en forma de metodos
    // Los nombres de estos metodos sean el alias de los nombres de los atributos de la base de datos
    Integer getIdOrder();
    String getCustomerName();
    LocalDateTime getOrderDate();
    Double getOrderTotal();
    String getPizzaNames();
}
