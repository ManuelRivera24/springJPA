package com.platzi.pizza.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass // Esta anotacion sirve pàra indicar que esta clase puede ser extendida o heredada a partir de otros Entitys
public class AuditableEntity {
    // Se crearon estas dos columnas dentro de la base de datos para hacer las auditorias
    @Column(name = "created_date")
    @CreatedDate // Para que la auditoria funcione se utilizan estas anotaciones propias para este proceso
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
