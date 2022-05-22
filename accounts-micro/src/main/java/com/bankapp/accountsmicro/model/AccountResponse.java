package com.bankapp.accountsmicro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@SuperBuilder
public class AccountResponse {
    private Long accountNumber;
    private String message;
}