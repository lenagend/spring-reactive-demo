package com.kkm.springReactiveDemo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@DataMongoTest // <1>
public class MongoDbSliceTest {

    @Autowired ItemRepository repository; // <2>

    @Test // <3>
    void itemRepositorySavesItems() {
        Item sampleItem = new Item( //
                "name", "description", 1.99);

        repository.save(sampleItem) //
                .as(StepVerifier::create) //
                .expectNextMatches(item -> {
                    assertThat(item.getId()).isNotNull();
                    assertThat(item.getName()).isEqualTo("name");
                    assertThat(item.getDescription()).isEqualTo("description");
                    assertThat(item.getPrice()).isEqualTo(1.99);

                    return true;
                }) //
                .verifyComplete();
    }
}
// end::code[]