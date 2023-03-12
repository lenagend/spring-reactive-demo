package com.kkm.springReactiveDemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FluxTransformingTest {

    @Test
    public void skipAFew() {
        Flux<String> countFlux = Flux.just(
                        "one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        //skip() 오퍼레이션으로 앞 3개를 건너뛴 스트림으로 변환

        StepVerifier.create(countFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    @Test
    public void skipAFewSeconds() {
        Flux<String> countFlux = Flux.just(
                        "one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        //항목간에 1초동안 지연되는 delayElements 선언 후 처음 4초를 스킵했으므로 마지막 2개가 방출
        StepVerifier.create(countFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    @Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
                .take(3);

        //take()는 처음부터 지정된 수의 항목만을 방출한다

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void takeForAwhile() {
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500));
        //skip처럼 take도 시간을 기준으로 하는 다른 형태를 갖는다
        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        //스트림과 마찬가지로 람다식를 조건식으로 하여 필터링할 수 있다

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete();
    }

    @Test
    public void distinct() {
        Flux<String> animalFlux = Flux.just(
                        "dog", "cat", "bird", "dog", "bird", "anteater")
                .distinct();

        //sql명령어처럼 고유한 문자열만 방출한다 distinct
        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "anteater")
                .verifyComplete();
    }

    @Test
    public void map() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();

        //map() 오퍼레이션은 변환을 수행하는 Flux를 생성한다
        //map()에서 알아 둘 중요한 것은 각 항목이 Flux로부터 발행될 때 동,기,적으로 매핑이 수행된다는 것이다(순차적 처리)
        //따라서 비동기적으로(병행 처리)매핑을 수행하고 싶다면 flatMap() 오퍼레이션을 사용해야 한다
    }

    @Test
    public void flatMap() {
        //플랫맵에서는 각 객체를 새로운 Mono나 Flux로 매핑하며 해당 Mono나 Flux들의
        //결과는 하나의 새로운 flux가 된다
        //flatMap을 subsribeOn()과 사용하면 리액터 타입의 변환을 비동기적으로 수행할 수 있다
        //flatMap에서는 각 객체를 새로운 Mono나 Flux로 매핑하며 해당 Mono나 Flux들의 결과는 하나의 새로운 Flux가 된다


        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]);
                        })
                        .subscribeOn(Schedulers.parallel())
                );

        //Map연산만했다면 Flux는 Player 객체만을 전달할 것이며 동기적(순차적)으로 생성된다
        //subscribeOn은 subscribe와 유사하지만
        //subscribe()는 이름이 동사형이면서 리액티브 플로우를 구독 요청하고 실제로 구독하는 반면
        //subscribeOn은 이름이 서술적이면서 구독이 동시적으로 처리되어야 한다는 것을 지정한다
        //이때 Scheduler의 static 메서드 중 하나를 사용한다


        List<Player> playerList = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr"));

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .verifyComplete();
    }


    private static class Player {
        public Player(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return Objects.equals(firstName, player.firstName) && Objects.equals(lastName, player.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, lastName);
        }

        private final String firstName;
        private final String lastName;
    }

}