package ru.maxawergy.rsocketserver;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.maxawergy.rsocketserver.model.Cat;
import ru.maxawergy.rsocketserver.repository.CatRepository;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MainSocketControllerTest {


    @Autowired
    private CatRepository catRepository;

    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 5200);
    }

    @AfterEach
    public void cleanup() {
        requester.dispose();
    }

    @Test
    public void testGetCat() {
        Cat cat = new Cat();
        cat.setName("TestCat");
        cat.setAge(2);
        cat.setColor("Black");
        cat.setBreed("Siamese");

        Cat savedCat = catRepository.save(cat);

        Mono<Cat> result = requester.route("getCat")
                .data(savedCat.getId())
                .retrieveMono(Cat.class);

        assertNotNull(result.block());
    }

    @Test
    public void testAddCat() {
        Cat cat = new Cat();
        cat.setName("TestCat");
        cat.setAge(2);
        cat.setColor("Black");
        cat.setBreed("Siamese");

        Mono<Cat> result = requester.route("addCat")
                .data(cat)
                .retrieveMono(Cat.class);

        Cat savedCat = result.block();
        assertNotNull(savedCat);
        assertNotNull(savedCat.getId());
        assertTrue(savedCat.getId() > 0);
    }

    @Test
    public void testGetCats() {
        Flux<Cat> result = requester.route("getCats")
                .retrieveFlux(Cat.class);

        assertNotNull(result.blockFirst());
    }

    @Test
    public void testDeleteCat() {
        Cat cat = new Cat();
        cat.setName("TestCat");
        cat.setAge(2);
        cat.setColor("Black");
        cat.setBreed("Siamese");

        Cat savedCat = catRepository.save(cat);

        Mono<Void> result = requester.route("deleteCat")
                .data(savedCat.getId())
                .send();

        result.block();
        Cat deletedCat = catRepository.findCatById(savedCat.getId());
        assertNotSame(deletedCat, savedCat);
    }

}
