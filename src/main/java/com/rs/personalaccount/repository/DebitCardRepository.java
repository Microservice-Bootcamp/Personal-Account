package com.rs.personalaccount.repository;

import com.rs.personalaccount.entity.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {
}
