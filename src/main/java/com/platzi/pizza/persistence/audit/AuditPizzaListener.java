package com.platzi.pizza.persistence.audit;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import org.springframework.util.SerializationUtils;

public class AuditPizzaListener {

    private PizzaEntity currentValue;

    // Esta anotacion se va a ejecutar despues de que ocurra un select y la informacion sea cargada en un entity
    @PostLoad // Este metodo unicamente se va a ejecutar cuando un select obtenga alguna informacion (o algunos datos)
    public void postLoad(PizzaEntity entity) {
        System.out.println("POST LOAD");
        this.currentValue = SerializationUtils.clone(entity);
    }

    // Este metodo se ejecuta después de que ocurran los eventos (por ejemplo: actualizar o guardar)
    @PostPersist
    @PostUpdate
    public void onPostPersist(PizzaEntity entity) {
        System.out.println("POST PERSIST OR UPDATE");
        System.out.println("OLD VALUE");
        System.out.println("NEW VALUE: " + entity.toString());
    }

    @PreRemove // Quiere decir que este metodo se ejecutara antes de realizar el proceso de eliminacion en la base de datos
    public void onPreDelete(PizzaEntity entity) {
        System.out.println(entity.toString());
    }
}
