package com.rs.personalaccount.repository;

import com.rs.personalaccount.entity.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Flux<BankAccount> findAllByIdUser(String idUser);
    Mono<Boolean> existsAllByIdUserAndTypeAccount(String idUser, String typeAccount);
    Mono<Boolean> existsByIdUser(String idUser);
}
