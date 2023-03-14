package com.kkm.springReactiveDemo;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.*;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = ApiItemController.class) //웹플럭스 컨트롤러 테스트에 필요한 내용만 자동설정
@AutoConfigureRestDocs // 스프링 레스트 독 사용에 필요한 내용을 자동 설정
public class ApiItemControllerDocumentationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean InventoryService service;
    //@WebFluxㅆTest는 웹플럭스 컨트롤러 테스트에 필요한 내용만 자동설정하므로 service도 주입되지 않는다. @MockBean이 mock 객체를 주입해준다.
    @MockBean ItemRepository repository;

    @Test
    void findingAllItems() {
        when(repository.findAll()).thenReturn( // <1>
                Flux.just(new Item("item-1", "Alf alarm clock", //
                        "nothing I really need", 19.99)));

        this.webTestClient.get().uri("/api/items") //
                .exchange() //
                .expectStatus().isOk() //
                .expectBody() //
                .consumeWith(document("findAll", preprocessResponse(prettyPrint()))); // <2>
                //document()는 스프링 레스트 독 정적 메소드이며, 문서 생성기능을 테스트에 추가한다. 문서는 target/generated-snippets/findAll 디렉터리에 생성된다

    }

    @Test
    void postNewItem() {
        when(repository.save(any())).thenReturn( //
                Mono.just(new Item("1", "Alf alarm clock", "nothing important", 19.99)));

        this.webTestClient.post().uri("/api/items") // <1>
                .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99)) // <2>
                .exchange() //
                .expectStatus().isCreated() // <3>
                .expectBody() //
                .consumeWith(document("post-new-item", preprocessResponse(prettyPrint()))); // <4>
    }
    @Test
    void findOneItem() {
        when(repository.findById("item-1")).thenReturn( //
                Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))); // <1>

        this.webTestClient.get().uri("/api/items/item-1") //
                .exchange() //
                .expectStatus().isOk() //
                .expectBody() //
                .consumeWith(document("findOne", preprocessResponse(prettyPrint()))); // <2>
    }

    @Test
    void updateItem() {
        when(repository.save(any())).thenReturn( //
                Mono.just(new Item("1", "Alf alarm clock", "updated", 19.99)));

        this.webTestClient.put().uri("/api/items/1") // <1>
                .bodyValue(new Item("Alf alarm clock", "updated", 19.99)) // <2>
                .exchange() //
                .expectStatus().isOk() // <3>
                .expectBody() //
                .consumeWith(document("update-item", preprocessResponse(prettyPrint()))); // <4>
    }
}
