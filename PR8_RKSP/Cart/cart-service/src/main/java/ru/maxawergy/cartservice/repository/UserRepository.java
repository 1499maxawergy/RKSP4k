package ru.maxawergy.cartservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxawergy.cartservice.dto.ItemDTO;
import ru.maxawergy.cartservice.model.Item;
import ru.maxawergy.cartservice.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_item (user_id, item_id, count) VALUES (:userId, :itemId, :quantity) ON CONFLICT (user_id, item_id) DO UPDATE SET count = user_item.count + :quantity", nativeQuery = true)
    void addItemToUser(Long userId, Long itemId, int quantity);


    @Modifying
    @Transactional
    @Query(value = "UPDATE user_item SET count = CASE WHEN count > :quantity THEN count - :quantity ELSE 0 END WHERE user_id = :userId AND item_id = :itemId", nativeQuery = true)
    void updateItemQuantity(Long userId, Long itemId, int quantity);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_item WHERE user_id = :userId AND item_id = :itemId AND count <= 0", nativeQuery = true)
    void deleteItemIfCountIsZero(Long userId, Long itemId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_item WHERE user_id = :userId", nativeQuery = true)
    void clearUserCart(Long userId);

    @Modifying
    @Query(value = "SELECT item_id as itemId, count FROM user_item WHERE user_id = :userId", nativeQuery = true)
    List<Object[]> getCart(Long userId);


}
