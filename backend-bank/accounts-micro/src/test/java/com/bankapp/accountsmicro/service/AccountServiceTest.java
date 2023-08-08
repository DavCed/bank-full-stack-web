package com.bankapp.accountsmicro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import com.bankapp.accountsmicro.entity.Account;
import com.bankapp.accountsmicro.model.AccountDTO;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.model.Transaction;
import com.bankapp.accountsmicro.repo.AccountRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepo accountRepo;

    @Test
    void testSaveBankAccountInDB() {
        AccountDTO accountDTO = new AccountDTO(1,1,1234567890,9876543210L,'C', 1500.0,'P');

        AccountResponse result = accountService.saveBankAccountInDB(accountDTO);

        assertNotNull(result);
        assertEquals(accountDTO.userId(), result.getUserId());
        // Add more assertions for other fields
        assertEquals(AccountService.PENDING, result.getAccountStatus());
        assertEquals("Bank account opened!", result.getMessage());
    }

    @Test
    void testFetchBankAccountsByUserIdInDB() {
        Integer userId = 123;
        List<Account> accounts = new ArrayList<>(); // Add mock account data
        when(accountRepo.findAllByUserId(userId)).thenReturn(Optional.of(accounts));

        List<AccountResponse> result = accountService.fetchBankAccountsByUserIdInDB(userId);

        assertNotNull(result);
    }

    @Test
    void testFetchAllBankAccountsInDB() {
        List<Account> accounts = new ArrayList<>(); // Add mock account data
        when(accountRepo.findAll()).thenReturn(accounts);

        List<AccountResponse> result = accountService.fetchAllBankAccountsInDB();

        assertNotNull(result);
    }

    @Test
    void testUpdateBankAccountBalanceInDB() {
        Transaction transaction = new Transaction(500,1234567890,'W');
        Account account = new Account(1,1,1234567890,9876543210L,'C', 1500.0,'A');
        when(accountRepo.findByAccountNumber(transaction.accountNumber())).thenReturn(Optional.of(account));

        AccountResponse result = accountService.updateBankAccountBalanceInDB(transaction);

        assertNotNull(result);
    }

    @Test
    void testUpdateBankAccountStatusInDB() {
        AccountDTO accountDTO = new AccountDTO(1,1,1234567890,9876543210L,'C', 1500.0,'P');
        Account account = new Account(1,1,1234567890,9876543210L,'C', 1500.0,'P');
        when(accountRepo.findByAccountNumber(accountDTO.accountNumber())).thenReturn(Optional.of(account));

        AccountResponse result = accountService.updateBankAccountStatusInDB(accountDTO);

        assertNotNull(result);
    }

    @Test
    void testAddAccountType() {
        String result = accountService.addAccountType('C');
        assertEquals(AccountService.CHECKING, result);

        result = accountService.addAccountType('S');
        assertEquals(AccountService.SAVINGS, result);
    }

    @Test
    void testAddAccountStatus() {
        String result = accountService.addAccountStatus('A');
        assertEquals(AccountService.APPROVED, result);

        result = accountService.addAccountStatus('P');
        assertEquals(AccountService.PENDING, result);

        result = accountService.addAccountStatus('D');
        assertEquals(AccountService.DENIED, result);
    }
}