package com.rs.personalaccount.service;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.repository.BankAccountRepository;

import com.rs.personalaccount.util.WebClientTemplate;
import com.rs.personalaccount.vo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


@Log4j2
@Service
public class BankAccountService {


    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private WebClientTemplate webClientTemplate;

    public Mono<BankAccount> saveBankAccount(BankAccount bankAccount){
        return existUserWithOneAccount(bankAccount.getDniUser(), bankAccount.getTypeAccount())
                .flatMap(value -> validateSaveWithCriteri(value, bankAccount,bankAccount.getTypeAccount()));

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




    public Mono<Boolean> findUserAccountByAccountNumber(Integer accountNumber){
        return bankAccountRepository.existsByAccountNumber(accountNumber);

    }

    /**
     * to create open account first check if the user is registered
     * @param dniNumber Integer(number of dni)
     * @param typeAccount String
     * @return boolean
     */
    private Mono<Boolean> existUserWithOneAccount(Integer dniNumber, String typeAccount){
        return userIsRegistered(dniNumber)
                .flatMap(value->validateIfUserIsRegistered(value,dniNumber,typeAccount));
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
    private Mono<UserRegistered> userIsRegistered(Integer dniNumber){
        return webClientTemplate.templateWebClient("http://localhost:8092")
                .get()
                .uri("/user/status/"+dniNumber)
                .retrieve()
                .bodyToMono(UserRegistered.class);
    }
    private Mono<VipCustomer> isUserVip(Integer dniNumber){
        return webClientTemplate.templateWebClient("http://localhost:8092")
                .get()
                .uri("/user/person/"+dniNumber)
                .retrieve()
                .bodyToMono(VipCustomer.class);
    }

    private Mono<UserCredit> userVipHaveCredit(Integer dniNumber){
        return webClientTemplate.templateWebClient("http://localhost:8093")
                .get()
                .uri("/credit/status/"+dniNumber)
                .retrieve()
                .bodyToMono(UserCredit.class);
    }

    private Mono<Boolean> validateIfUserIsRegistered (UserRegistered value, Integer dniNumber, String typeAccount){
        if(value.getStatus()){
            return bankAccountRepository.existsByDniUserAndTypeAccount(dniNumber,typeAccount);
        }
        else{
            return Mono.just(false);
        }
    }

    /**
     * user can only have account of each type
     * balance must be greater than 0
     * type account must be ahorro OR corriente OR fijo
     * check if is Vip user
     * for vip customer must have credit
     * @return response as BankAccount
     */
    private Mono<BankAccount> validateSaveWithCriteri(Boolean value, BankAccount bankAccount, String typeAccount){
        return isUserVip(bankAccount.getDniUser())
                .flatMap(condition ->{
                    /*if(!value && bankAccount.getBalance()>=0 && accountTypeAccepted.test(typeAccount)&& condition.getStatus().equals(true)){
                        bankAccount.setBenefitStatus(true);
                        return bankAccountRepository.save(bankAccount);
                    }
                    return Mono.empty();/*
                     */
                    return userVipHaveCredit(bankAccount.getDniUser())
                            .flatMap(haveCredit->{
                                if(!value && bankAccount.getBalance()>=0 && accountTypeAccepted.test(typeAccount)){
                                    if((condition.getStatus().equals(true) && haveCredit.getStatus().equals(true))){
                                        bankAccount.setBenefitStatus(true);
                                    }
                                    return bankAccountRepository.save(bankAccount);
                                }
                                return Mono.empty();

                            });
                });
    }

    Predicate<String> accountTypeAccepted = type ->type.equals("ahorro") || type.equals("corriente") || type.equals("fijo");

    public Mono<ResumeProduct> allAccountByDni (Integer dniNumber){
        return bankAccountRepository.findAllByDniUser(dniNumber)
                .filter(account -> account.getDniUser()!=null)
                .switchIfEmpty(Mono.empty())
                .collectList()
                .flatMap(account -> {
                    var resume = new ResumeProduct();
                    Map<String, Object> lol = new HashMap<>();
                    //List<Map<String,Object>> accounts = new ArrayList<>();
                    //account.forEach(element -> accounts.add(1,2));
                    account.forEach(element -> lol.put(element.getTypeAccount(),element.getAccountNumber()));
                    //accounts.add(lol);
                    resume.setDniUser(dniNumber);
                    resume.setAccounts(lol);
                    return  Mono.just(resume);
                });
    }


}
