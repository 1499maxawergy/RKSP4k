package ru.maxawergy.webfluxserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.maxawergy.webfluxserver.exception.CustomException;
import ru.maxawergy.webfluxserver.model.Cat;
import ru.maxawergy.webfluxserver.repository.CatRepository;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatRepository catRepository;

    @Autowired
    public CatController(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Cat>> getCatById(@PathVariable Long id) {
        return catRepository.findById(id)
                .map(cat -> ResponseEntity.ok(cat))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Cat> getAllCats(@RequestParam(name = "minage", required = false) Integer minAge) {
        Flux<Cat> cats = catRepository.findAll();

        if (minAge != null && minAge > 0) {
            // Если параметр "minage" указан, применяем фильтрацию
            cats = cats.filter(cat -> cat.getAge() >= minAge);
        }

        return cats
                .map(this::transformCat) // Применение оператора map для преобразования объектов Cat
                .onErrorResume(e -> {
                    // Обработка ошибок
                    return Flux.error(new CustomException("Failed to fetch cats", e));
                })
                .onBackpressureBuffer(); // Работа в формате backpressure
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cat> createCat(@RequestBody Cat cat) {
        return catRepository.save(cat);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cat>> updateCat(@PathVariable Long id, @RequestBody Cat updatedCat) {
        return catRepository.findById(id)
                .flatMap(existingCat -> {
                    existingCat.setName(updatedCat.getName());
                    existingCat.setColor(updatedCat.getColor());
                    existingCat.setAge(updatedCat.getAge());
                    existingCat.setBreed(updatedCat.getBreed());
                    return catRepository.save(existingCat);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCat(@PathVariable Long id) {
        return catRepository.findById(id)
                .flatMap(existingCat ->
                        catRepository.delete(existingCat)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Cat transformCat(Cat cat) {
        // Пример преобразования объекта Cat
        cat.setName(cat.getName().toUpperCase()); // Преобразование имени кота в верхний регистр
        return cat;
    }
}
