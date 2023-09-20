package ru.maxawergy.rsocketclient.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.maxawergy.rsocketclient.dto.CatListWrapper;
import ru.maxawergy.rsocketclient.model.Cat;

import java.util.List;


@RestController
@RequestMapping("/api/cats")
public class ChannelController {

    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }


    @PostMapping("/exp")
    public Flux<Cat> addCatsMultiple(@RequestBody CatListWrapper catListWrapper){
        List<Cat> catList = catListWrapper.getCats();
        Flux<Cat> cats = Flux.fromIterable(catList);
        return rSocketRequester
                .route("catChannel")
                .data(cats)
                .retrieveFlux(Cat.class);
    }

}
