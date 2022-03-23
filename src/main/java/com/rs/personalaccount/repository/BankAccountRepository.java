package com.rs.personalaccount.repository;

import com.rs.personalaccount.entity.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableReactiveMongoRepositories
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Mono<BankAccount> findByAccountNumber(Integer accountNumber);
    Mono<Boolean> existsByDniUserAndTypeAccount(Integer dniNumber, String typeAccount);
    //Flux<BankAccount> findAllByDniUser(Integer dniUser);
    Mono<Boolean> existsByAccountNumber(Integer accountNumber);
    Mono<Boolean> existsByIdBankAccount(String id);
    Flux<BankAccount> findAllByDniUser(Integer dniNumber);



}
