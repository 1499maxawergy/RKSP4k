package ru.maxawergy.rsocketclient.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxawergy.rsocketclient.model.Cat;


@RestController
@RequestMapping("/api/cats")
public class RequestStreamController {

    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }


    @GetMapping
    public Publisher<Cat> getCats() {
        return rSocketRequester
                .route("getCats")
                .data(new Cat())
                .retrieveFlux(Cat.class);
    }
}
