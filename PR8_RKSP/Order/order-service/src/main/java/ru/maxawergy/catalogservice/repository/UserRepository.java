package ru.maxawergy.catalogservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxawergy.catalogservice.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_item WHERE user_id = :userId", nativeQuery = true)
    void clearUserCart(Long userId);


    @Modifying
    @Query(value = "SELECT item_id as itemId, count FROM user_item WHERE user_id = :userId", nativeQuery = true)
    List<Object[]> getCart(Long userId);
}