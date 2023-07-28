package com.bankapp.accountsmicro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Integer amount;
    private String account;
    private String transactionType;
}
