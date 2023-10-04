package ru.maxawergy.catalogservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxawergy.catalogservice.model.Item;
import ru.maxawergy.catalogservice.repository.ItemRepository;

import java.util.List;

@Service
public class CatalogService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    @Transactional
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    @Transactional
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
    @Transactional
    public Item updateItem(Long id, Item updatedItem) {
        Item existingItem = itemRepository.findById(id).orElse(null);
        if (existingItem != null) {
            existingItem.setName(updatedItem.getName());
            existingItem.setPrice(updatedItem.getPrice());
            return itemRepository.save(existingItem);
        }
        return null;
    }
    @Transactional
    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
