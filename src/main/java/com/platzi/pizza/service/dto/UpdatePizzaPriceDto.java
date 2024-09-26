package com.platzi.pizza.service.dto;

import lombok.Data;

@Data // Esta anotacion es para que Lombok nos cree los getters, setters y los constructores obligatorios de la clase
public class UpdatePizzaPriceDto {
    private int pizzaId;
    private double newPrice;
}
