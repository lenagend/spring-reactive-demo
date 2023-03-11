package com.kkm.springReactiveDemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

        //이처럼 Flux나 Mono의 항목들을 콘솔로 출력하면 리액티브 타입이 실제 작동하는 것을 파악하는 데 좋다
        //그러나 리액터의 StepVerifier를 사용하는 것이 Flux나 Mono를 테스트하는 더 좋은 방법이다
        //Flux나 Mono가 지정되면 StepVerifier는 해당 리액티브 타입을 구독한 다음에 스트림을 통해 전달되는 데이터에 대해 assertion을 제공한다

        StepVerifier.create(fruitFlux)
                .expectNext("apple")
                .expectNext("banana")
                .expectNext("grape")
                .expectNext("strawberry")
                .expectNext("oragne")
                .verifyComplete();
    }


}
