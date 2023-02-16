package com.greglturnquist.hackingspringboot.reactive;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Greg Turnquist
 */
// tag::1[]
@ExtendWith(SpringExtension.class) // <1>
public class BlockHoundIntegrationTest {

    AltInventoryService inventoryService; // <2>

    @MockBean ItemRepository itemRepository; // <3>
    @MockBean CartRepository cartRepository;
    // end::1[]

    // tag::2[]
    @BeforeEach
    void setUp() {
        // Define test data <1>

        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        // Define mock interactions provided
        // by your collaborators <2>

        when(cartRepository.findById(anyString())) //
                .thenReturn(Mono.<Cart> empty().hide()); // <3>

        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new AltInventoryService(itemRepository, cartRepository);
    }
    // end::2[]

    // tag::3[]
    @Test
    void blockHoundShouldTrapBlockingCall() { //
        Mono.delay(Duration.ofSeconds(1)) // <1>
                .flatMap(tick -> inventoryService.addItemToCart("My Cart", "item1")) // <2>
                .as(StepVerifier::create) // <3>
                .verifyErrorSatisfies(throwable -> { // <4>
                    assertThat(throwable).hasMessageContaining( //
                            "block()/blockFirst()/blockLast() are blocking");
                });
    }
    // end::3[]
}
// end::code[]