package com.bankapp.accountsmicro.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.bankapp.accountsmicro.entity.Account;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class AccountRepoTest {

    @Test
    void testFindByAccountNumber() {
        Integer accountNumber = 12345;
        Account expectedAccount = new Account(1,1,accountNumber,9876543210L,'S',150000.0,'A');

        AccountRepo accountRepoMock = mock(AccountRepo.class);

        // Mock the behavior of the repository
        when(accountRepoMock.findByAccountNumber(accountNumber)).thenReturn(Optional.of(expectedAccount));

        // Call the method and verify the result
        Optional<Account> result = accountRepoMock.findByAccountNumber(accountNumber);
        assertTrue(result.isPresent());
        assertEquals(expectedAccount, result.get());
    }

    @Test
    void testFindAllByUserId() {
        Integer userId = 456;
        List<Account> expectedAccounts = Arrays.asList(
                new Account(1,1,123215378,9876543210L,'S',150000.0,'A'),
                new Account(2,1,823746327,9876543210L,'C',150000.0,'A')
        );

        AccountRepo accountRepoMock = mock(AccountRepo.class);

        // Mock the behavior of the repository
        when(accountRepoMock.findAllByUserId(userId)).thenReturn(Optional.of(expectedAccounts));

        // Call the method and verify the result
        Optional<List<Account>> result = accountRepoMock.findAllByUserId(userId);
        assertTrue(result.isPresent());
        assertEquals(expectedAccounts, result.get());
    }
}