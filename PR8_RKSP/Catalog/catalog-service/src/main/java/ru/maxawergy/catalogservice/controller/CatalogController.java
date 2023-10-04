package ru.maxawergy.catalogservice.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maxawergy.catalogservice.model.Item;
import ru.maxawergy.catalogservice.service.CatalogService;
import ru.maxawergy.catalogservice.validator.TokenValidator;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private TokenValidator tokenValidator;

    private boolean validate(String token) {
        return tokenValidator.validateUser(token) != null;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(HttpServletRequest request) {
        if (validate(request.getHeader("Authorization"))) {
            List<Item> items = catalogService.getAllItems();
            return ResponseEntity.ok(items);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }

    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> getItemById(HttpServletRequest request, @PathVariable Long id) {
        if (validate(request.getHeader("Authorization"))) {
            Item item = catalogService.getItemById(id);
            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
    }

    @PostMapping("/item")
    public ResponseEntity<?> createItem(HttpServletRequest request, @RequestBody Item item) {
        if (validate(request.getHeader("Authorization"))) {
            Item createdItem = catalogService.createItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> updateItem(HttpServletRequest request, @PathVariable Long id, @RequestBody Item updatedItem) {
        if (validate(request.getHeader("Authorization"))) {
            Item updated = catalogService.updateItem(id, updatedItem);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> deleteItem(HttpServletRequest request, @PathVariable Long id) {
        if (validate(request.getHeader("Authorization"))) {
            if (catalogService.deleteItem(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Forbidden");
        }
    }
}