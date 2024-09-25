package com.platzi.pizza.web.controller;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaService pizzaService; // Estamos inyectando el servicio que acabamos de construir

    @Autowired // Indica que estamos inyectando esta dependencia de PizzaService
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<List<PizzaEntity>> getAll(){
        return ResponseEntity.ok(this.pizzaService.getAll());
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> get(@PathVariable int idPizza){
        return ResponseEntity.ok(this.pizzaService.get(idPizza));
    }

    @PostMapping // Ahora la peticion no es get sino post (porque vamos a recibir informacion)
    public ResponseEntity<PizzaEntity> add(@RequestBody PizzaEntity pizza){ // La anotacion nos dice que vamos a recibir el cuerpo para anotarla
        if(pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza())){
        return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping // Put para actualizar
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza){ // La anotacion nos dice que vamos a recibir el cuerpo para anotarla
        if(pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable int idPizza) {
        if(this.pizzaService.exists(idPizza)){
            this.pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
