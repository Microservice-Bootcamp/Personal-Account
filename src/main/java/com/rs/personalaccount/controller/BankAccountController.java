package com.rs.personalaccount.controller;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/save")
    public Mono<BankAccount> saveAccount(@RequestBody BankAccount bankAccount){
        return bankAccountService.saveBankAccount(bankAccount);
    }

    @GetMapping("/all")
    public Flux<BankAccount> findAllAcountBank(){
        return bankAccountService.findAllAcountBank();
    }


}
