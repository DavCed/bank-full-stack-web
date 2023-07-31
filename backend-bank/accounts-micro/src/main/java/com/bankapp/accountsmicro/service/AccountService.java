package com.bankapp.accountsmicro.service;

import com.bankapp.accountsmicro.entity.Account;
import com.bankapp.accountsmicro.exceptions.InvalidAccountException;
import com.bankapp.accountsmicro.exceptions.InvalidTransactionException;
import com.bankapp.accountsmicro.model.*;
import com.bankapp.accountsmicro.repo.AccountRepo;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;
    private static final String CHECKING ="Checking";
    private static final String SAVINGS ="Savings";
    private static final Random rand = new Random();

    public AccountResponse saveBankAccountInDB(AccountDTO accountDTO){
        log.info("Opening bank account for user id " + accountDTO.userId() + "....");
        if(accountDTO.balance() <= 0 )
            throw new InvalidAccountException("Cannot open bank account with zero or negative balance....");
        Account account = Account.builder()
                .userId(accountDTO.userId())
                .accountNumber(accountDTO.accountNumber())
                .routingNumber(accountDTO.routingNumber())
                .accountType(accountDTO.accountType())
                .balance(accountDTO.balance())
                .build();
        int accNum = rand.nextInt(999999999) + 1234567890;
        account.setAccountNumber(accNum < 0 ? -(accNum) : accNum);
        account.setRoutingNumber(9876543210L);
        Account accountDB = accountRepo.save(account);
        return AccountResponse.builder()
                .userId(accountDB.getUserId())
                .accountNumber(accountDB.getAccountNumber())
                .routingNumber(accountDB.getRoutingNumber())
                .accountType(accountDB.getAccountType() == 'C' ? CHECKING : SAVINGS)
                .balance(accountDB.getBalance())
                .message("Bank account opened!")
                .build();
    }

    public List<AccountResponse> fetchBankAccountsByUserIdInDB(Integer userId){
        log.info("Fetching bank accounts for user id " + userId + "....");
        return accountRepo.findAllByUserId(userId).orElseThrow()
                .stream()
                .map(account -> AccountResponse.builder()
                        .userId(userId)
                        .accountNumber(account.getAccountNumber())
                        .routingNumber(account.getRoutingNumber())
                        .accountType(account.getAccountType() == 'C' ? CHECKING : SAVINGS)
                        .balance(account.getBalance())
                        .message("Fetched bank account number " + account.getAccountNumber())
                        .build())
                .toList();
    }

    public List<AccountResponse> fetchAllBankAccountsInDB(){
        log.info("Fetching all bank accounts....");
        return accountRepo.findAll()
                .stream()
                .map(account -> AccountResponse.builder()
                        .userId(account.getUserId())
                        .accountNumber(account.getAccountNumber())
                        .routingNumber(account.getRoutingNumber())
                        .accountType(account.getAccountType() == 'C' ? CHECKING : SAVINGS)
                        .balance(account.getBalance())
                        .message("Fetched bank account number " + account.getAccountNumber())
                        .build())
                .toList();
    }

    public AccountResponse updateBankAccountInDB(Transaction transaction){
        log.info("Making transaction on bank account for $" + transaction.amount() + "....");
        Account accountDB = accountRepo.findByAccountNumber(transaction.accountNumber()).orElseThrow();
        if (transaction.amount() <= 0)
            throw new InvalidTransactionException("Cannot make transaction of zero or negative amount....");
        else if (transaction.transactionType() == 'W'
                && transaction.amount() > accountDB.getBalance())
            throw new InvalidTransactionException("Cannot withdraw more than current balance....");
        accountDB.setBalance(transaction.transactionType() == 'W'
                ? accountDB.getBalance() - transaction.amount()
                : accountDB.getBalance() + transaction.amount());
        Account accountUpdated = accountRepo.save(accountDB);
        return AccountResponse.builder()
                .userId(accountUpdated.getUserId())
                .accountNumber(accountUpdated.getAccountNumber())
                .routingNumber(accountUpdated.getRoutingNumber())
                .accountType(accountUpdated.getAccountType() == 'C' ? CHECKING : SAVINGS)
                .balance(accountUpdated.getBalance())
                .message("Transaction complete!")
                .build();
    }
}