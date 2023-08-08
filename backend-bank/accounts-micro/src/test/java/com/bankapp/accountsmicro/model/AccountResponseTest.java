package com.bankapp.accountsmicro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountResponseTest {

    @Test
    void testAccountResponseBuilderAndGetters() {
        Integer userId = 1;
        Integer accountNumber = 12345;
        Long routingNumber = 987654321L;
        String accountType = "Savings";
        double balance = 1000.0;
        String accountStatus = "Active";
        String message = "Account created successfully";

        AccountResponse accountResponse = AccountResponse.builder()
                .userId(userId)
                .accountNumber(accountNumber)
                .routingNumber(routingNumber)
                .accountType(accountType)
                .balance(balance)
                .accountStatus(accountStatus)
                .message(message)
                .build();

        assertEquals(userId, accountResponse.getUserId());
        assertEquals(accountNumber, accountResponse.getAccountNumber());
        assertEquals(routingNumber, accountResponse.getRoutingNumber());
        assertEquals(accountType, accountResponse.getAccountType());
        assertEquals(balance, accountResponse.getBalance(), 0.0);
        assertEquals(accountStatus, accountResponse.getAccountStatus());
        assertEquals(message, accountResponse.getMessage());
    }

    @Test
    void testAccountResponseEqualsAndHashCode() {
        AccountResponse accountResponse1 = AccountResponse.builder()
                .userId(1)
                .accountNumber(12345)
                .routingNumber(987654321L)
                .accountType("Savings")
                .balance(1000.0)
                .accountStatus("Active")
                .message("Account created successfully")
                .build();

        AccountResponse accountResponse2 = AccountResponse.builder()
                .userId(1)
                .accountNumber(12345)
                .routingNumber(987654321L)
                .accountType("Savings")
                .balance(1000.0)
                .accountStatus("Active")
                .message("Account created successfully")
                .build();

        assertEquals(accountResponse1, accountResponse2);
        assertEquals(accountResponse1.hashCode(), accountResponse2.hashCode());
    }
}