package com.rs.personalaccount.controller;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.service.BankAccountService;
import com.rs.personalaccount.vo.AccountBalance;
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

    @GetMapping("/{number}")
    public Mono<Boolean> findAccount(@PathVariable("number") Integer accoutNmber){
        return bankAccountService.findUserAccountByAccountNumber(accoutNmber);
    }

    @GetMapping("/detail/{account}")
    public Mono<BankAccount> getPersonalBankAccount(@PathVariable("account") Integer accountNumber){
        return bankAccountService.findAccountNumber(accountNumber);
    }

    @PutMapping("/update")
    public Mono<String> updateBankAccount(@RequestBody BankAccount bankAccount){
        return bankAccountService.updateBalanceAccount(bankAccount);
    }

    @GetMapping("/balance/{balance}")
    public Mono<AccountBalance> getAccountBalance(@PathVariable("balance") Integer accountNumber){
        return bankAccountService.getBalanceOfAccount(accountNumber);
    }
}
