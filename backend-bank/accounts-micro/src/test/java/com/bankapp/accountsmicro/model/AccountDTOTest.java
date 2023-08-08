package com.bankapp.accountsmicro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDTOTest {

    @Test
    void testAccountDTOConstructorAndGetters() {
        Integer accountId = 1;
        Integer userId = 2;
        Integer accountNumber = 12345;
        Long routingNumber = 987654321L;
        Character accountType = 'S';
        double balance = 1000.0;
        Character accountStatus = 'A';

        AccountDTO accountDTO = new AccountDTO(
                accountId, userId, accountNumber, routingNumber, accountType, balance, accountStatus
        );

        assertEquals(accountId, accountDTO.accountId());
        assertEquals(userId, accountDTO.userId());
        assertEquals(accountNumber, accountDTO.accountNumber());
        assertEquals(routingNumber, accountDTO.routingNumber());
        assertEquals(accountType, accountDTO.accountType());
        assertEquals(balance, accountDTO.balance(), 0.0);
        assertEquals(accountStatus, accountDTO.accountStatus());
    }

    @Test
    void testAccountDTOEqualsAndHashCode() {
        AccountDTO accountDTO1 = new AccountDTO(1, 2, 12345, 987654321L, 'S', 1000.0, 'A');
        AccountDTO accountDTO2 = new AccountDTO(1, 2, 12345, 987654321L, 'S', 1000.0, 'A');

        assertEquals(accountDTO1, accountDTO2);
        assertEquals(accountDTO1.hashCode(), accountDTO2.hashCode());
    }
}