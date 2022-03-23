package com.rs.personalaccount.service;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.entity.DebitCard;
import com.rs.personalaccount.repository.BankAccountRepository;
import com.rs.personalaccount.repository.DebitCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Mono<DebitCard> saveDebitCard(DebitCard debitCard) {
        return bankAccountRepository.findAllByDniUser(debitCard.getDniUser())
                .collectList()
                .switchIfEmpty(Mono.empty())
                .flatMap(x -> {
                    log.info("Initialization of save method bl...");
                    BankAccount principalAccount = x.stream().filter(account -> account.getAccountNumber() == debitCard.getPrincipalBankAccount()).findFirst().orElse(null);

                    if(principalAccount == null) {
                        return Mono.empty();
                    }

                    debitCard.setCardNumber(UUID.randomUUID().toString());

                    x.forEach(account -> debitCard.addBankAccount(account.getAccountNumber()));

                    return debitCardRepository.save(debitCard);
                });
    }
}
