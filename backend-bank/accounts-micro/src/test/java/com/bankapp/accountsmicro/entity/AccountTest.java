package com.bankapp.accountsmicro.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountTest {

    @InjectMocks
    Account account;

    @Test
    void testAccountIdGetterAndSetter() {
        Integer accountId = 123;
        account.setAccountId(accountId);
        assertEquals(accountId, account.getAccountId());
    }

    @Test
    void testUserIdGetterAndSetter() {
        Integer userId = 456;
        account.setUserId(userId);
        assertEquals(userId, account.getUserId());
    }

    @Test
    void testAccountNumberGetterAndSetter() {
        Integer accountNumber = 789;
        account.setAccountNumber(accountNumber);
        assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    void testRoutingNumberGetterAndSetter() {
        Long routingNumber = 987654321L;
        account.setRoutingNumber(routingNumber);
        assertEquals(routingNumber, account.getRoutingNumber());
    }

    @Test
    void testAccountTypeGetterAndSetter() {
        Character accountType = 'S';
        account.setAccountType(accountType);
        assertEquals(accountType, account.getAccountType());
    }

    @Test
    void testBalanceGetterAndSetter() {
        double balance = 1000.0;
        account.setBalance(balance);
        assertEquals(balance, account.getBalance(), 0.0);
    }

    @Test
    void testAccountStatusGetterAndSetter() {
        Character accountStatus = 'A';
        account.setAccountStatus(accountStatus);
        assertEquals(accountStatus, account.getAccountStatus());
    }
}