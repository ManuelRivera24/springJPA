package com.platzi.pizza.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "pizza")
// Las siguientes tres anotaciones son de Lombook, y sirven para ahorrar codigo al hacer automaticamente los getters, setters y el constructor
@Getter
@Setter
@NoArgsConstructor // Genera el constructor sin parametros (constructor vacio)
public class PizzaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Con esta anotacion se va incrementando el valor del id de uno en uno
    @Column(name = "id_pizza", nullable = false) // Indica que el valor no puede ser nulo
    private Integer idPizza;

    @Column(nullable = false, length = 30, unique = true) // No puede existir dos pizzas con el mismo nombre
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false, columnDefinition = "DECIMAL(5,2)") // De esta manera Hibernate (es lo que por defecto usa spring para JPA) sabra como debe crear esta columna price
    private Double price;

    @Column(columnDefinition = "TINYINT")
    private Boolean vegetarian;

    @Column(columnDefinition = "TINYINT")
    private Boolean vegan;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean available;

    // En esta parte iria la relacion que tiene OrderItemEntity con PizzaEntity, pero no es necesaria por ahora
//    @OneToOne(mappedBy = "pizza")
//    private OrderItemEntity pizza;
}
