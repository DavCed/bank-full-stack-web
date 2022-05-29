package com.bankapp.accountsmicro.service;

import com.bankapp.accountsmicro.entity.Account;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.repo.AccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    public AccountResponse recordAccountInDB(Account account){
        account.setAccountNumber(new Random().nextLong() + 99999999L);
        account.setRoutingNumber(12345678901234L);
        String accountType = account.getAccountType() == 'C' ? "Checking" : "Savings";
        log.info("Attempting to open " + accountType + " account...");
        int count = (int) accountRepo.findAll().stream()
                .filter(accountInDB -> accountInDB.getAccountType() == account.getAccountType()
                        & accountInDB.getUserId().equals(account.getUserId()))
                .count();
        String message;
        if(count == 0){
            accountRepo.save(account);
            message = accountType + " account opened!";
        } else {
            message = accountType + " account already exists.";
        }
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .userId(account.getUserId())
                .routingNumber(account.getRoutingNumber())
                .balance(account.getBalance())
                .accountType(accountType)
                .message(message)
                .build();
    }

    public List<AccountResponse> findAccountsByUserIdInDB(Integer userId){
        log.info("Searching for bank accounts for user by ID " + userId + "....");
        return accountRepo.findAll().stream()
                .filter(account -> Objects.equals(account.getUserId(), userId))
                .map(account ->
                        AccountResponse.builder()
                                .accountNumber(account.getAccountNumber())
                                .userId(userId)
                                .routingNumber(account.getRoutingNumber())
                                .balance(account.getBalance())
                                .accountType(account.getAccountType() == 'C' ? "Checking" : "Savings")
                                .message("Users bank accounts retrieved.")
                                .build())
                .collect(Collectors.toList());
    }

    public List<AccountResponse> findAllAccountsInDB(){
        log.info("Fetching all bank accounts in system....");
        return accountRepo.findAll().stream()
                .map(account ->
                        AccountResponse.builder()
                                .accountNumber(account.getAccountNumber())
                                .userId(account.getUserId())
                                .routingNumber(account.getRoutingNumber())
                                .balance(account.getBalance())
                                .accountType(account.getAccountType() == 'C' ? "Checking" : "Savings")
                                .message("All bank accounts retrieved.")
                                .build())
                .collect(Collectors.toList());
    }
}