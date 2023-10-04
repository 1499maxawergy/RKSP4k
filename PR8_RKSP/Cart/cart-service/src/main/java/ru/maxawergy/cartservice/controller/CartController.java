package ru.maxawergy.cartservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maxawergy.cartservice.dto.ItemDTO;
import ru.maxawergy.cartservice.model.User;
import ru.maxawergy.cartservice.repository.UserRepository;
import ru.maxawergy.cartservice.validator.TokenValidator;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    private User validate(String token) {
        return tokenValidator.validateUser(token);
    }


    @GetMapping("")
    public ResponseEntity<?> getAllCart(HttpServletRequest request){
        User user = validate(request.getHeader("Authorization"));
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        List<Object[]> itemsData = userRepository.getCart(user.getId());
        List<ItemDTO> items = new ArrayList<>();

        for (Object[] itemData : itemsData) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemId((Long) itemData[0]);
            itemDTO.setCount((int) itemData[1]);
            items.add(itemDTO);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/clear")
    public ResponseEntity<?> clearCart(HttpServletRequest request){
        User user = validate(request.getHeader("Authorization"));
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        userRepository.clearUserCart(user.getId());
        return ResponseEntity.ok("Cart is clear");
    }

    @PostMapping("")
    public ResponseEntity<?> addItemToCart(HttpServletRequest request, @RequestBody ItemDTO itemDTO){
        User user = validate(request.getHeader("Authorization"));
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        if (itemDTO.getCount() < 1){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Bad count");
        }
        userRepository.addItemToUser(user.getId(), itemDTO.getItemId(), itemDTO.getCount());
        return ResponseEntity.ok("Successfully updated");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteItemFromCart(HttpServletRequest request, @RequestBody ItemDTO itemDTO){
        User user = validate(request.getHeader("Authorization"));
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        if (itemDTO.getCount() < 1){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Bad count");
        }
        userRepository.updateItemQuantity(user.getId(), itemDTO.getItemId(), itemDTO.getCount());
        userRepository.deleteItemIfCountIsZero(user.getId(), itemDTO.getItemId());
        return ResponseEntity.ok("Successfully deleted");
    }

    @GetMapping("/order")
    public ResponseEntity<?> makeOrder(HttpServletRequest request){
        User user = validate(request.getHeader("Authorization"));
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        String token = user.getToken();

        return ResponseEntity.ok(orderServiceFeignClient.getOrderInfo(token).getBody());
    }
}
