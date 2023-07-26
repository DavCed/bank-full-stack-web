package com.bankapp.accountsmicro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@SuperBuilder
public class AccountResponse {
    private Integer accountNumber;
    private String accountType;
    private Long routingNumber;
    private double balance;
    private String message;
    private Integer userId;
}