package ru.maxawergy.rsocketserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.maxawergy.rsocketserver.model.Cat;
import ru.maxawergy.rsocketserver.repository.CatRepository;

@Controller
public class MainSocketController {

    private final CatRepository catRepository;

    @Autowired
    public MainSocketController(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @MessageMapping("getCat")
    public Mono<Cat> getCat(Long id) {
        return Mono.justOrEmpty(catRepository.findCatById(id));
    }

    @MessageMapping("addCat")
    public Mono<Cat> addCat(Cat cat) {
        return Mono.justOrEmpty(catRepository.save(cat));
    }

    @MessageMapping("getCats")
    public Flux<Cat> getCats() {
        return Flux.fromIterable(catRepository.findAll());
    }

    @MessageMapping("deleteCat")
    public Mono<Void> deleteCat(Long id){
        Cat cat = catRepository.findCatById(id);
        catRepository.delete(cat);
        return Mono.empty();
    }

    @MessageMapping("catChannel")
    public Flux<Cat> catChannel(Flux<Cat> cats){
        // block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-3
        // return Flux.fromIterable(catRepository.saveAll(cats.collectList().block()));
        // Используем Mono.fromCallable, чтобы асинхронно вызвать метод catRepository::save для каждого кота и вернуть результаты как Flux.
        return cats.flatMap(cat -> Mono.fromCallable(() -> catRepository.save(cat)))
                .collectList()
                .flatMapMany(savedCats -> Flux.fromIterable(savedCats));

    }
}
