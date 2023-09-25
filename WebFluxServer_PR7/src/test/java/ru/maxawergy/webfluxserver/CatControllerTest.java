package ru.maxawergy.webfluxserver;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.maxawergy.webfluxserver.controller.CatController;
import ru.maxawergy.webfluxserver.model.Cat;
import ru.maxawergy.webfluxserver.repository.CatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CatControllerTest {

    @Test
    public void testGetCatById() {
        // Создайте фиктивного кота
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Whiskers");

        // Создайте мок репозитория
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findById(1L)).thenReturn(Mono.just(cat));

        // Создайте экземпляр контроллера
        CatController catController = new CatController(catRepository);

        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Cat> response = catController.getCatById(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cat, response.getBody());
    }

    @Test
    public void testGetAllCats() {
        // Создайте список фиктивных котов
        Cat cat1 = new Cat();
        cat1.setId(1L);
        cat1.setName("Whiskers");
        cat1.setAge(3);

        Cat cat2 = new Cat();
        cat2.setId(2L);
        cat2.setName("Fluffy");
        cat2.setAge(4);

        // Создайте мок репозитория
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findAll()).thenReturn(Flux.just(cat1, cat2));

        // Создайте экземпляр контроллера
        CatController catController = new CatController(catRepository);

        // Вызовите метод контроллера и проверьте результат
        Flux<Cat> response = catController.getAllCats(null);
        assertEquals(2, response.collectList().block().size());
    }

    @Test
    public void testCreateCat() {
        // Создайте фиктивного кота
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Whiskers");

        // Создайте мок репозитория
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.save(cat)).thenReturn(Mono.just(cat));

        // Создайте экземпляр контроллера
        CatController catController = new CatController(catRepository);

        // Вызовите метод контроллера и проверьте результат
        Mono<Cat> response = catController.createCat(cat);
        assertEquals(cat, response.block());
    }

    @Test
    public void testUpdateCat() {
        // Создайте фиктивного кота
        Cat existingCat = new Cat();
        existingCat.setId(1L);
        existingCat.setName("Whiskers");

        // Создайте фиктивного обновленного кота
        Cat updatedCat = new Cat();
        updatedCat.setId(1L);
        updatedCat.setName("Fluffy");

        // Создайте мок репозитория
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findById(1L)).thenReturn(Mono.just(existingCat));
        when(catRepository.save(existingCat)).thenReturn(Mono.just(updatedCat));

        // Создайте экземпляр контроллера
        CatController catController = new CatController(catRepository);

        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Cat> response = catController.updateCat(1L, updatedCat).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCat, response.getBody());
    }

    @Test
    public void testDeleteCat() {
        // Создайте фиктивного кота
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Whiskers");

        // Создайте мок репозитория
        CatRepository catRepository = Mockito.mock(CatRepository.class);
        when(catRepository.findById(1L)).thenReturn(Mono.just(cat));
        when(catRepository.delete(cat)).thenReturn(Mono.empty());

        // Создайте экземпляр контроллера
        CatController catController = new CatController(catRepository);

        // Вызовите метод контроллера и проверьте результат
        ResponseEntity<Void> response = catController.deleteCat(1L).block();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}

