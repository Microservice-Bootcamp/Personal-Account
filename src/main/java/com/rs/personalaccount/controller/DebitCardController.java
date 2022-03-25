package com.rs.personalaccount.controller;

import com.rs.personalaccount.entity.DebitCard;
import com.rs.personalaccount.service.DebitCardService;
import com.rs.personalaccount.vo.AccountBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debit-card")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @PostMapping("/save")
    public Mono<ResponseEntity<DebitCard>> saveDebitCard(@RequestBody DebitCard debitCard){
        return debitCardService.saveDebitCard(debitCard)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<DebitCard>> findAll(){
        return new ResponseEntity<>(debitCardService.findAll(), HttpStatus.OK);

    }

    @GetMapping("/card-number/{cardNumber}")
    public ResponseEntity<Mono<DebitCard>> findByCardNumber(@PathVariable String cardNumber) {
        return new ResponseEntity<>(debitCardService.findByCardNumber(cardNumber), HttpStatus.OK);
    }

    @GetMapping("/balance/{cardNumber}")
    public Mono<ResponseEntity<AccountBalance>> getAccountBalance(@PathVariable String cardNumber){
        return debitCardService.getBalanceOfAccount(cardNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

}
