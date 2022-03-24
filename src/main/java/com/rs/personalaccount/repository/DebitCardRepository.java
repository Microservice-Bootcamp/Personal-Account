package com.rs.personalaccount.repository;

import com.rs.personalaccount.entity.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Mono;

import java.util.List;

@EnableReactiveMongoRepositories
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {

    Mono<DebitCard> findByBankAccounts_AccountNumberIn(List<Integer> accountNumber);

    Mono<DebitCard> findByCardNumber(String cardNumber);
}
