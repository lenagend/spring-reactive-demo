package com.kkm.springReactiveDemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluxCreationTests {
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

    @Test
    public void createAFlux_fromArray() {
        String[] fruits = new String[] {
                "Apple", "Orange", "Grape", "Banana", "Strawberry" };

        Flux<String> fruitFlux = Flux.fromArray(fruits);
        //배열로부터 Flux를 얻는다 Flux.fromArray

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromIterable() {
        List<String> fruitList = new ArrayList<>();
        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitList);
        //List, Set,Iterable의 다른 구현 컬렉션으로부터 Flux를 생성해야 한다면 fromIterable를 호출하면 된다

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromStream() {
        Stream<String> fruitStream =
                Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);
        //또는 Flux를 생성하는 소스로 자바 Stream 객체를 사용해야 한다면 static 메서드인 fromStream()을 호출하면된다.

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_range() {
        Flux<Integer> intervalFlux =
                Flux.range(1, 5);
        //데이터 없이 매번 새 값으로 증가하는 수자를 방출하는 Flux를 원할때

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createAFlux_interval() {
        Flux<Long> intervalFlux =
                Flux.interval(Duration.ofSeconds(1))
                        .take(5);

        //range()와 유사한 메서드 interval()은 마찬가지로 증가값을 방출하는데 시작값과 종료값 대신 
        //값이 방출하는 시간 간격이나 주기를 지정한다
        //매초마다 값을 방출하는 flux를 생성하는 코드이다
        //0부터 시작하여 값이 증가한다
        //무한정 실행되기때문에 take()로 첫번째 다섯개 항목으로 제한한 모습이다
        
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

}
