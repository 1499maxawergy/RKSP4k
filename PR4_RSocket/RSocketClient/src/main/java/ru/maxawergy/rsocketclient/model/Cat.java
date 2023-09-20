package ru.maxawergy.rsocketclient.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Cat {
    private Long id;
    private String name;
    private Integer age;
    private String color;
    private String breed;
}
