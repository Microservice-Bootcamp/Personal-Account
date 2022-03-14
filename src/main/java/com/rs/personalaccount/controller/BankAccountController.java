package com.rs.personalaccount.controller;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.service.BankAccountService;
import com.rs.personalaccount.vo.AccountBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/save")
    public Mono<ResponseEntity< BankAccount>> saveAccount(@RequestBody BankAccount bankAccount){
        return bankAccountService.saveBankAccount(bankAccount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/all")
    public ResponseEntity< Flux<BankAccount>> findAllAcountBank(){
        return new ResponseEntity<>(bankAccountService.findAllAcountBank(), HttpStatus.OK);
    }

    @GetMapping("/{number}")
    public Mono<Boolean> findAccount(@PathVariable("number") Integer accoutNmber){
        return bankAccountService.findUserAccountByAccountNumber(accoutNmber);
    }

    @GetMapping("/detail/{account}")
    public Mono<ResponseEntity< BankAccount>> getPersonalBankAccount(@PathVariable("account") Integer accountNumber){
        return bankAccountService.findAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<String>> updateBankAccount(@RequestBody BankAccount bankAccount){
        //return new ResponseEntity<>(bankAccountService.updateBalanceAccount(bankAccount), HttpStatus.CREATED);
        return bankAccountService.updateBalanceAccount(bankAccount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/balance/{balance}")
    public Mono<ResponseEntity<AccountBalance>> getAccountBalance(@PathVariable("balance") Integer accountNumber){
        return bankAccountService.getBalanceOfAccount(accountNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
}
