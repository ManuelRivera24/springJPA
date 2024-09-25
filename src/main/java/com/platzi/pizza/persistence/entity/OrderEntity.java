package com.platzi.pizza.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pizza_order")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", nullable = false)
    private Integer idOrder;

    @Column(name = "id_customer", nullable = false, length = 15)
    private Integer idCustomer;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "DECIMAL(6,2)")
    private Double total;

    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private String method;

    @Column(name = "additional_notes", length = 200)
    private String additionalNotes;

    // Con esta indicacion "fetch = FetchType.LAZY" estamos mejorando el rendimiento, al evitar hacer consultas innecesarias
    @OneToOne(fetch = FetchType.LAZY) // Esto (LAZY) es para que cargue esta relacion sino hasta que se use (si no se usa el no va a cargar la informacion que viene a partir de esta relacion)
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer", insertable = false, updatable = false)
    @JsonIgnore // Esta anotacion es para que no nos traiga toda la infomacion de los clientes (pero no es la mejor solucion porque igualmente se sigue haciendo la consulta, solo que no se muestra)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) // EAGER significa que cuando tratemos de recuperar un OrderEntity automaticamente tambien me traiga esta relacion
    private List<OrderItemEntity> items; // La relacion de orderEntity y orderItemEntity es diferente, porque son varios items los que pueden estar dentro de una misma orden
}
