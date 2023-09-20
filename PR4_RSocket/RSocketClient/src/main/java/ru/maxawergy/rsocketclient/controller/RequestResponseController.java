package ru.maxawergy.rsocketclient.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.maxawergy.rsocketclient.model.Cat;


@RestController
@RequestMapping("/api/cats")
public class RequestResponseController {

    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }


    @GetMapping("/{id}")
    public Mono<Cat> getCat(@PathVariable Long id) {
        return rSocketRequester
                .route("getCat")
                .data(id)
                .retrieveMono(Cat.class);
    }

    @PostMapping
    public Mono<Cat> addCat(@RequestBody Cat cat) {
        return rSocketRequester
                .route("addCat")
                .data(cat)
                .retrieveMono(Cat.class);
    }

}
