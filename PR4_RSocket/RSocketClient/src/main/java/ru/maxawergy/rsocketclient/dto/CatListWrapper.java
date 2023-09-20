package ru.maxawergy.rsocketclient.dto;

import reactor.core.publisher.Flux;
import ru.maxawergy.rsocketclient.model.Cat;

import java.util.List;

public class CatListWrapper {
    private List<Cat> cats;

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }
}
