package com.platzi.pizza.web.controller;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.PizzaService;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    // Este metodo tambien se cambio, ahora no retorna una lista sino que retorna una pagina
    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int elements){ // Ya no es @PathVariable, sino @RequestParam porque vamos a recibir parametros para esa consulta
        return ResponseEntity.ok(this.pizzaService.getAll(page, elements));
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

    // Este metodo se cambio (ya no va a retornar una lista sino que va a retornar una pagina)
    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailable(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements,
                                                          @RequestParam(defaultValue = "price") String sortBy,
                                                          @RequestParam(defaultValue = "ASC") String sortDirection) { // Este nuevo parametro sirve para elegir si queremos ordenar ascendente o descendente
        return ResponseEntity.ok(this.pizzaService.getAvailable(page, elements, sortBy, sortDirection)); // La pagina que queremos, cuantos elementos por pagina y por que queremos ordenar nuestra consulta
    }

    @GetMapping("/name/{name}") // Se le agrega un parametro porque ya existe uno que recibe solo un parametro
    public ResponseEntity<PizzaEntity> getByName(@PathVariable String name) {
        return ResponseEntity.ok(this.pizzaService.getByName(name));
    }

    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWith(@PathVariable String ingredient) {
        return ResponseEntity.ok(this.pizzaService.getWhith(ingredient));
    }

    @GetMapping("/without/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWithout(@PathVariable String ingredient) {
        return ResponseEntity.ok(this.pizzaService.getWhithout(ingredient));
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getCheapestPizzas(@PathVariable double price) {
        return ResponseEntity.ok(this.pizzaService.getCheapest(price));
    }

    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto) {
        if(this.pizzaService.exists(dto.getPizzaId())) {
            this.pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
