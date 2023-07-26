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
        int accNum = new Random().nextInt(999999999) + 1234567890;
        account.setAccountNumber(accNum < 0 ? -(accNum) : accNum);
        account.setRoutingNumber(9876543210L);
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

    public AccountResponse updateAccountInDB(List<String> details){
        log.info(details.toString());
        Account account = accountRepo.findByAccountNumber(Integer.parseInt(details.get(0))).get();
        account.setBalance(details.get(2).equals(String.valueOf('W'))
                ? account.getBalance() - Integer.parseInt(details.get(1))
                : account.getBalance() + Integer.parseInt(details.get(1)));
        Account accountDB = accountRepo.save(account);
        return AccountResponse.builder()
                .userId(accountDB.getAccountId())
                .accountType("Checking")
                .accountNumber(accountDB.getAccountNumber())
                .routingNumber(accountDB.getRoutingNumber())
                .balance(accountDB.getBalance())
                .message("Transaction complete!")
                .build();
    }
}