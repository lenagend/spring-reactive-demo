package com.kkm.springReactiveDemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
public class ApiItemController {

    private final ItemRepository repository;

    public ApiItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/itmes")
    Flux<Item> findAll(){
        return this.repository.findAll();
    }

    @GetMapping("/api/items/{id}")
    Mono<Item> findOne(@PathVariable String id){
        return this.repository.findById(id);

    }

    @PostMapping("/api/items")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item){//@RequestBody 애너테이션은 요청 본문을 인자로 만든다.
        // 인자 타입이 Mono이므로 이 요청 처리를 위한 리액티브 플로우에서 구독이 발생하지 않으면 본문을 Item으로 역직렬화 하는 과정도 실행되지 않는다.

        return  item.flatMap(s -> this.repository.save(s))
                .map(savedItem -> ResponseEntity
                        .created(URI.create("/api/items" +
                                savedItem.getId()))
                        .body(savedItem));

    }
}
