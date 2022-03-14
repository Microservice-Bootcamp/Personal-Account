package com.rs.personalaccount.service;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.repository.BankAccountRepository;

import com.rs.personalaccount.vo.AccountBalance;
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

    public Mono<String> updateBalanceAccount(BankAccount bankAccount){
        return bankAccountRepository.existsByIdBankAccount(bankAccount.getIdBankAccount())
                .flatMap(condition->{
                    if(condition.equals(true)){
                        return bankAccountRepository.save(bankAccount)
                                .flatMap(result->Mono.empty());
                    }
                    return Mono.empty();
                });

        //return bankAccountRepository.save(bankAccount)
                //.flatMap(mm->Mono.empty());
    }

    private Mono<Boolean> existUserWithAccountBank(String iduser){
        return bankAccountRepository.existsByIdUser(iduser);
    }
    public Mono<Boolean> findUserAccountByAccountNumber(Integer accountNumber){
        return bankAccountRepository.existsByAccountNumber(accountNumber);

    }

    /**
     * to create open account first check if the user is registered
     * @param temporalIdUser String(NameOfUser)
     * @param typeAccount String
     * @return boolean
     */
    private Mono<Boolean> existUserWithOneAccount(String temporalIdUser, String typeAccount){
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

    public Mono<BankAccount> findAccountNumber(Integer accountNumber){
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    public Mono<AccountBalance> getBalanceOfAccount(Integer accountNumber){

        return  bankAccountRepository.findByAccountNumber(accountNumber)
                .flatMap(value-> Mono.just(new AccountBalance(value.getBalance())));
                //.defaultIfEmpty(new AccountBalance(0));



    }

}
