package com.rs.personalaccount.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rs.personalaccount.vo.Account;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "debitCard")
public class DebitCard {

    @Id
    private String idDebitCard;

    private String cardNumber;

    private List<Account> bankAccounts = new ArrayList<>();

    private Integer dniUser;

    private Boolean isActive;

    @JsonIgnore
    public void addBankAccount(Account bankAccount) {
        this.bankAccounts.add(bankAccount);
    }

}
