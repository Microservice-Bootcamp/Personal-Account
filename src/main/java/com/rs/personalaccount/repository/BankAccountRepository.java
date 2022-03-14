package com.rs.personalaccount.repository;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.repository.custom.CustomPersonalAccountRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Mono;

@EnableReactiveMongoRepositories
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String>, CustomPersonalAccountRepository {

    Mono<BankAccount> findByAccountNumber(Integer accountNumber);
    Mono<Boolean> existsAllByIdUserAndTypeAccount(String idUser, String typeAccount);
    Mono<Boolean> existsByIdUser(String idUser);
    Mono<Boolean> existsByAccountNumber(Integer accountNumber);
    Mono<Boolean> existsByIdBankAccount(String id);



}
