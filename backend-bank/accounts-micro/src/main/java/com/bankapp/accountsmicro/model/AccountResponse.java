package com.bankapp.accountsmicro.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class AccountResponse {
    private Integer userId;
    private Integer accountNumber;
    private Long routingNumber;
    private String accountType;
    private double balance;
    private String accountStatus;
    private String message;
}