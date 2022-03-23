package com.rs.personalaccount.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {

    private Integer accountNumber;
    private LocalDateTime associateDebitCardDate;
    private Boolean flagPrincipal;
}
