package com.greglturnquist.hackingspringboot.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author Greg Turnquist
 */
class BlockHoundUnitTest {

    // tag::obvious-failure[]
    @Test
    void threadSleepIsABlockingCall() {
        Mono.delay(Duration.ofSeconds(1)) // <1>
                .flatMap(tick -> {
                    try {
                        Thread.sleep(10); // <2>
                        return Mono.just(true);
                    } catch (InterruptedException e) {
                        return Mono.error(e);
                    }
                }) //
                .as(StepVerifier::create) //
                .verifyComplete();
//				.verifyErrorMatches(throwable -> {
//					assertThat(throwable.getMessage()) //
//							.contains("Blocking call! java.lang.Thread.sleep");
//					return true;
//				});

    }
    // end::obvious-failure[]

}