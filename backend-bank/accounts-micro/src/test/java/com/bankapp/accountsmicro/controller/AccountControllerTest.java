package com.bankapp.accountsmicro.controller;

import com.bankapp.accountsmicro.model.AccountDTO;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.model.Transaction;
import com.bankapp.accountsmicro.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    AccountController accountController;

    @Mock
    AccountService accountService;

    @Test
    void testSaveBankAccount() {
        // Prepare a mock AccountDTO object
        AccountDTO mockAccountDTO = new AccountDTO(1,1,1234567890,9876543210L,'C',1000.0,'P');
        AccountResponse expectedResponse = new AccountResponse(1,1234567890,9876543210L,"Checking",1000.0,"Pending","Saved");

        // Mock the behavior of the service
        when(accountService.saveBankAccountInDB(any(AccountDTO.class))).thenReturn(expectedResponse);

        // Perform the POST request to add a bank account
        ResponseEntity<AccountResponse> response = accountController.saveBankAccount(mockAccountDTO);

        // Assert the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned AccountResponse object matches the expected response
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testFetchBankAccountsByUserId() {
        // Prepare a mock user ID
        Integer userId = 1;
        List<AccountResponse> expectedResponse = Arrays.asList(
                new AccountResponse(1,1234567890,9876543210L,"Checking",1000.0,"Pending","Fetched"),
                new AccountResponse(1,231564078,9876543210L,"Savings",1500.0,"Pending","Fetched")
        );

        // Mock the behavior of the service
        when(accountService.fetchBankAccountsByUserIdInDB(userId)).thenReturn(expectedResponse);

        // Perform the GET request to fetch bank accounts by user ID
        ResponseEntity<List<AccountResponse>> response = accountController.fetchBankAccountsByUserId(userId);

        // Assert the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned list of AccountResponse objects matches the expected responses
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testFetchAllBankAccounts() {
        List<AccountResponse> expectedResponses = Arrays.asList(
                new AccountResponse(1,1234567890,9876543210L,"Checking",1000.0,"Pending","Fetched"),
                new AccountResponse(2,367592090,9876543210L,"Checking",2000.0,"Approved","Fetched")
        );

        // Mock the behavior of the service
        when(accountService.fetchAllBankAccountsInDB()).thenReturn(expectedResponses);

        // Perform the GET request to fetch all bank accounts
        ResponseEntity<List<AccountResponse>> response = accountController.fetchAllBankAccounts();

        // Assert the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned list of AccountResponse objects matches the expected responses
        assertEquals(expectedResponses, response.getBody());
    }

    @Test
    void testUpdateBankAccountBalance() {
        // Prepare a mock Transaction object
        Transaction mockTransaction = new Transaction(500,1234567890,'D');
        AccountResponse expectedResponse = new AccountResponse(1,1234567890,9876543210L,"Checking",1000.0,"Approved","Transacted");

        // Mock the behavior of the service
        when(accountService.updateBankAccountBalanceInDB(any(Transaction.class))).thenReturn(expectedResponse);

        // Perform the PUT request to update bank account balance
        ResponseEntity<AccountResponse> response = accountController.updateBankAccountBalance(mockTransaction);

        // Assert the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned AccountResponse object matches the expected response
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testUpdateBankAccountStatus() {
        // Prepare a mock AccountDTO object with updated status
        AccountDTO mockAccountDTO = new AccountDTO(1,1,1234567890,9876543210L,'C',1000.0,'A');
        AccountResponse expectedResponse = new AccountResponse(1,1234567890,9876543210L,"Checking",1000.0,"Approved","Updated");

        // Mock the behavior of the service
        when(accountService.updateBankAccountStatusInDB(any(AccountDTO.class))).thenReturn(expectedResponse);

        // Perform the PUT request to update bank account status
        ResponseEntity<AccountResponse> response = accountController.updateBankAccountStatus(mockAccountDTO);

        // Assert the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned AccountResponse object matches the expected response
        assertEquals(expectedResponse, response.getBody());
    }
}