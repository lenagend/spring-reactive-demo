package com.kkm.springReactiveDemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ReactivePractive1 {
    @Test
    public void createAFlux_just(){
        Flux<String> fruitFlux = Flux
                .just("apple", "banana", "grape", "strawberry", "oragne");
        //이경우 flux는 생성되지만 구독자가 없다.
        //구독자가 없이는 데이터가 전달되지 않는다.
        //구독자를 추가할때는 subscribe()메서드를 호출하면 된다
        fruitFlux.subscribe(
                f-> System.out.println("here's some fruits:" + f)
        );
    }


}
