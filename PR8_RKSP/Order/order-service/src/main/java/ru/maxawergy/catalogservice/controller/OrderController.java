package ru.maxawergy.catalogservice.controller;


import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxawergy.catalogservice.model.User;
import ru.maxawergy.catalogservice.repository.ItemRepository;
import ru.maxawergy.catalogservice.repository.UserRepository;
import ru.maxawergy.catalogservice.service.EmailService;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ItemRepository itemRepository;


    @GetMapping("/order")
    public ResponseEntity<?> getOrderInfo(@RequestParam(value = "token") String token) throws MessagingException {
        User user = userRepository.findUserByToken(token);
        if (user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
        StringBuilder messageBody = new StringBuilder("Поздравляем, вы сделали заказ: <br>");
        List<Object[]> itemsData = userRepository.getCart(user.getId());
        if (itemsData.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No items in cart");
        }
        for (Object[] itemData : itemsData) {
            messageBody.append("id: ").append(itemData[0]).append("<br>");
            messageBody.append("name: ").append(itemRepository.findItemsById((Long) itemData[0]).getName()).append("<br>");
            messageBody.append("count: ").append(itemData[1]).append("<br>");
        }
        userRepository.clearUserCart(user.getId());
        emailService.sendMessage(user.getEmail(), String.valueOf(messageBody));
        return ResponseEntity.ok("Successfully sent");
    }
}