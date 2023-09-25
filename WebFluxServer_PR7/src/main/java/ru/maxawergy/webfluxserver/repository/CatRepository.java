package ru.maxawergy.webfluxserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.maxawergy.webfluxserver.model.Cat;

@Repository
public interface CatRepository extends R2dbcRepository<Cat, Long> {
}
