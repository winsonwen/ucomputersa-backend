package com.ucomputersa.common.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@AllArgsConstructor
@Configuration
public class HibernateService {

    private PlatformTransactionManager transactionManager;


    public Mono<Void> synchronizeSession(Runnable runnable) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        return Mono.using(
                () -> transactionManager.getTransaction(def),
                ts -> Mono.fromRunnable(runnable)
                        .doOnSuccess(result -> transactionManager.commit(ts))
                        .doOnError(error -> transactionManager.rollback(ts)),
                ts -> {
                }
        ).then();

    }


    public <T> Mono<T> synchronizeSessionReactive(Supplier<T> supplier) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        return Mono.using(
                () -> transactionManager.getTransaction(def),
                ts -> Mono.fromCallable(supplier::get)
                        .doOnSuccess(result -> transactionManager.commit(ts)),
                ts -> {
                }
        );
    }

}