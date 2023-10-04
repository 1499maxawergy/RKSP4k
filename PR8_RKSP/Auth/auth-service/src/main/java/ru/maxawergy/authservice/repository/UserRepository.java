package ru.maxawergy.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxawergy.authservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(String id);
    User findUserByEmail(String email);
}
