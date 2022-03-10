package com.rs.personalaccount.service;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.repository.BankAccountRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class BankAccountService {

    private String tempUser = "juana";

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Mono<BankAccount> saveBankAccount(BankAccount bankAccount){
        return existUserWithOneAccount(tempUser, bankAccount.getTypeAccount())
                .flatMap(value ->{
                    if(!value){
                        return bankAccountRepository.save(bankAccount);
                    }
                    return Mono.empty();
                });

    }

    public Mono<Boolean> existUserWithAccountBank(String iduser){
        return bankAccountRepository.existsByIdUser(iduser);
    }

    public Mono<Boolean> existUserWithOneAccount(String temporalIdUser, String typeAccount){
        return existUserWithAccountBank(temporalIdUser)
                .flatMap(value->{
                    if(value){
                        return bankAccountRepository.existsAllByIdUserAndTypeAccount(temporalIdUser, typeAccount);
                    }
                    else{
                        return Mono.just(false);
                    }
                });
    }

    public Flux<BankAccount> findAllAcountBank(){
        return bankAccountRepository.findAll();
    }

}
