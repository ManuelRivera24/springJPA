package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.persistence.repository.PizzaPagSortRepository;
import com.platzi.pizza.persistence.repository.PizzaRepository;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import com.platzi.pizza.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import java.awt.print.Pageable;
import java.util.List;

@Service
public class PizzaService { // Vamos a tener consultas dentro de nuestro servicio
//    private final JdbcTemplate jdbcTemplate; // Esto es otra forma de hacerlo
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository; // Se inyecta el nuevo Repository

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    // Este metodo se modifico, ahora se hace con "PizzaPagSortRepository" para ordenar o paginar
    public Page<PizzaEntity> getAll(int page, int elements){
        Pageable pageRequest = (Pageable) PageRequest.of(page, elements);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    public PizzaEntity get(int idPizza){
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity save(PizzaEntity pizza){
        return this.pizzaRepository.save(pizza);
    }

    public boolean exists(int idPizza){
        return this.pizzaRepository.existsById(idPizza);
    }

    public void delete(int idPizza){
        this.pizzaRepository.deleteById(idPizza);
    }

    // Este metodo tambien se modifico, ahora se va a ejecutar a travez de "pizzaPagSortRepository" (ya no vamos a retornar una lista sino una pagina)
    public Page<PizzaEntity> getAvailable(int page, int elements, String sortBy, String sortDirection) { // Vamos a recibir un tercer elemento (sortBy)
        System.out.println("Total de pizzas vegetarianas: " + this.pizzaRepository.countByVeganTrue());

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = (Pageable) PageRequest.of(page, elements, sort); // Tambien le agregamos el tercer elemento utilizando la clase "sort"
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest); // Estamos llamando a la consulta que hicimos desde "PizzaRepository"
    }

    public PizzaEntity getByName(String name) {
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("La pizza no existe"));
    }

    public List<PizzaEntity> getWhith(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWhithout(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getCheapest(double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    // Esta anotacion se usa cuando tenemos dos o mas llamados a la base de datos en el mismo metodo (con esta anotacion garantizamos que se ejecute todo o no se ejecute nada, no deja el proceso a medias, como en el caso de actualizar un registro y luego guardarlo, o se cumplen las dos cosas o no se hace nada)
    @Transactional(noRollbackFor = EmailApiException.class) // Esta anotacion nos garantiza tener las cuatro caracteristicas "ACID" de manera cubierta y segura
    public void updatePrice(UpdatePizzaPriceDto dto) {
        this.pizzaRepository.updatePrice(dto); // En este parametro van las dos variables, internamente lo hace cuando se utiliza ese tipo de metodos
        this.sendEmail(); // Esta linea se puede comentar, solo se utilizó de ejemplo para mostrar la funcion de la anotacion "@Transactional"
    }

    private void sendEmail() {
        throw new EmailApiException();
    }
}
