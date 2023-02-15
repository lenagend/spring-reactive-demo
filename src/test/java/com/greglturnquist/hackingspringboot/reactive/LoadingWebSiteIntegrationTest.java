package com.greglturnquist.hackingspringboot.reactive;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_HTML;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Disabled("pom.xml에서 blockhound-junit-platform 의존 관계를 제거한 후에 실행해야 성공한다.")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //<1>
@AutoConfigureWebTestClient // <2>
public class LoadingWebSiteIntegrationTest {

    @Autowired WebTestClient client; // <3>

    @Test // <4>
    void test() {
        client.get().uri("/").exchange() //
                .expectStatus().isOk() //
                .expectHeader().contentType(TEXT_HTML) //
                .expectBody(String.class) //
                .consumeWith(exchangeResult -> {
                    assertThat(exchangeResult.getResponseBody()).contains("<a href=\"/add");
                });
    }
}
// end::code[]