package com.rs.personalaccount.repository.custom;


import com.rs.personalaccount.vo.AccountBalance;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

@Log4j2
public class CustomPersonalAccountRepositoryImpl implements CustomPersonalAccountRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public CustomPersonalAccountRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }


    @Override
    public Mono<AccountBalance> findByAccountNumber(Integer getOneValor, String nel) {
        Query query = new Query(Criteria.where("accountNumber").is(getOneValor));
        query.fields().include("balance");
       return reactiveMongoTemplate.findOne(query, AccountBalance.class);
    }
}
