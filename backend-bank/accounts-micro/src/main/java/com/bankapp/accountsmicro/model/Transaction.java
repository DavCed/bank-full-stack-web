package com.bankapp.accountsmicro.model;

public record Transaction(Integer amount,
                          Integer accountNumber,
                          Character transactionType){}
