package com.rs.personalaccount.controller;

import com.rs.personalaccount.entity.DebitCard;
import com.rs.personalaccount.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
