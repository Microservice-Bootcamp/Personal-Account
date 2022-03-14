package com.rs.personalaccount.repository.custom;

import com.rs.personalaccount.vo.AccountBalance;
import reactor.core.publisher.Mono;

public interface CustomPersonalAccountRepository {
    Mono<AccountBalance> findByAccountNumber(Integer accountNumber, String ga);
}
