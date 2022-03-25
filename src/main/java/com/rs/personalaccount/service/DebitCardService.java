package com.rs.personalaccount.service;

import com.rs.personalaccount.entity.BankAccount;
import com.rs.personalaccount.entity.DebitCard;
import com.rs.personalaccount.repository.BankAccountRepository;
import com.rs.personalaccount.repository.DebitCardRepository;
import com.rs.personalaccount.vo.Account;
import com.rs.personalaccount.vo.AccountBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Mono<DebitCard> saveDebitCard(DebitCard debitCard) {
        List<Integer> accounts = new ArrayList<>();
        debitCard.getBankAccounts().forEach(x -> accounts.add(x.getAccountNumber()));

        return bankAccountRepository.findAllByDniUserAndAccountNumberIn(debitCard.getDniUser(), accounts)
                .collectList()
                .flatMap(x -> {

                    if(x.isEmpty()) {
                        return Mono.empty();
                    }
                    log.info("Initialization of save method bl...");

                    Account principalAccount = debitCard.getBankAccounts().stream().filter(acc -> acc.getFlagPrincipal() == true).findFirst().orElse(null);

                    if(principalAccount == null) {
                        return Mono.empty();
                    }

                    debitCard.setCardNumber(UUID.randomUUID().toString());

                    List<Account> banks = new ArrayList<>();

                    debitCard.getBankAccounts().forEach(bkAccount -> {
                        bkAccount.setAccountNumber(bkAccount.getAccountNumber());
                        bkAccount.setAssociateDebitCardDate(LocalDateTime.now());
                        bkAccount.setFlagPrincipal(bkAccount.getFlagPrincipal());

                        banks.add(bkAccount);
                    });

                    return debitCardRepository.save(debitCard);
                });
    }

    public Flux<DebitCard> findAll() {
        return debitCardRepository.findAll();
    }

    public Mono<DebitCard> findByCardNumber(String cardNumber) {
        return debitCardRepository.findByCardNumber(cardNumber);
    }

    public Mono<AccountBalance> getBalanceOfAccount(String cardNumber){
        return debitCardRepository.findByCardNumber(cardNumber)
                .flatMap(x -> {
                    Account account = x.getBankAccounts().stream().filter(acc -> acc.getFlagPrincipal() == true)
                            .findFirst().orElse(null);

                    if(account == null) {
                        return Mono.empty();
                    }

                    return bankAccountRepository.findByAccountNumber(account.getAccountNumber())
                            .flatMap(value-> Mono.just(new AccountBalance(value.getBalance())));
                });
    }
}
