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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    public AccountResponse recordAccountInDB(Account account){
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
                .message(message)
                .build();
    }

    public List<Account> findAccountsByUserIdInDB(Integer userId){
        log.info("ACCOUNTS FOUND BY USER ID: " + userId + ".");
        return accountRepo.findAll().stream()
                .filter(account -> Objects.equals(account.getUserId(), userId))
                .collect(Collectors.toList());
    }

    public List<Account> findAllAccountsInDB(){
        log.info("ACCOUNTS FOUND!");
        return accountRepo.findAll();
    }
}