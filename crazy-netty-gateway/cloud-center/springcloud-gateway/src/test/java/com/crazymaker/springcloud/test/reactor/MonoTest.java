package com.crazymaker.springcloud.test.reactor;

import com.crazymaker.springcloud.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class MonoTest {

    @Test
    public void testThen1() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        Mono.just(BeanUtil.asList(1, 2, 3, 4))
                .doOnNext(i -> log.info(" do onext  i = {}, counter {}", i, counter.incrementAndGet()))
                .map(i -> {
                    log.info("map i = {},  counter  {}", i, counter.get());
                    return i;
                })
                .flatMap(i -> Mono.fromCallable(() -> {
                    log.info("flatMap i={}  counter  {}", i, counter.get());
                    return Mono.just(i.size());
                }))
                .then(thenFunction(counter))
                .block();
    }

    private static Mono<Integer> thenFunction(final AtomicInteger counter) {
        System.out.println("then " + counter.get());
        return Mono.just(2);
    }

    @Test
    public void testThen2() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        Mono.just(BeanUtil.asList(1, 2, 3, 4))
                .doOnNext(i -> log.info(" do onext  i = {}, counter {}", i, counter.incrementAndGet()))
                .map(i -> {
                    log.info("map i = {},  counter  {}", i, counter.get());
                    return i;
                })
                .flatMap(i -> Mono.fromCallable(() -> {
                    log.info("flatMap i={}  counter  {}", i, counter.get());
                    return Mono.just(i.size());
                }))
                .then(Mono.fromCallable(() -> {
                    log.info("then  counter  {}", counter.get());
                    return 2;
                }))
                .block();
    }


    @Test
    public void testThen3() throws Exception {

        final AtomicInteger counter = new AtomicInteger();
        Mono.just(BeanUtil.asList(1, 2, 3, 4))
                .doOnNext(i -> log.info(" do onext  i = {}, counter {}", i, counter.incrementAndGet()))
                .map(i -> {
                    log.info("map i = {},  counter  {}", i, counter.get());
                    return i;
                })
                .flatMap(i -> Mono.fromCallable(() -> {
                    log.info("flatMap i={}  counter  {}", i, counter.get());
                    return Mono.just(i.size());
                }))
                .then(Mono.defer(() -> Mono.fromCallable(() -> {
                    System.out.println("then " + counter.get());
                    return 2;
                })))
                .block();

    }
}