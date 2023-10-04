package ru.maxawergy.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxawergy.catalogservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByToken(String token);
}
