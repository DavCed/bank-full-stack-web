package com.bankapp.accountsmicro.model;

public record AccountDTO(Integer accountId,
                         Integer userId,
                         Integer accountNumber,
                         Long routingNumber,
                         Character accountType,
                         Double balance){}