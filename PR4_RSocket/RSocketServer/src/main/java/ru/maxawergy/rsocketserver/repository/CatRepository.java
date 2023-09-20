package ru.maxawergy.rsocketserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxawergy.rsocketserver.model.Cat;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    Cat findCatById(Long id);
}
