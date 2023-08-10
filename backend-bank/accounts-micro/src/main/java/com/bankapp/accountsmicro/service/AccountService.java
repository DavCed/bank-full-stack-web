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
    AccountRepo accountRepo;
    static final String CHECKING ="Checking";
    static final String SAVINGS ="Savings";
    static final String APPROVED = "Approved";
    static final String DENIED = "Denied";
    static final String PENDING = "Pending";
    static final Random rand = new Random();

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
                .accountStatus(accountDTO.accountStatus())
                .build();
        int accNum = rand.nextInt(999999999) + 1234567890;
        account.setAccountNumber(accNum < 0 ? -(accNum) : accNum);
        account.setRoutingNumber(9876543210L);
        account.setAccountStatus('P');
        accountRepo.saveAndFlush(account);
        return AccountResponse.builder()
                .userId(account.getUserId())
                .accountNumber(account.getAccountNumber())
                .routingNumber(account.getRoutingNumber())
                .accountType(addAccountType(account.getAccountType()))
                .balance(account.getBalance())
                .accountStatus(PENDING)
                .message("Bank account opened!")
                .build();
    }

    public List<AccountResponse> fetchBankAccountsByUserIdInDB(Integer userId){
        log.info("Fetching bank accounts for user id " + userId + "....");
        List<Account> accountsDB = accountRepo.findAllByUserId(userId)
                .orElseThrow(() -> new InvalidAccountException("No bank accounts exist for this user...."));
        return accountsDB.stream()
                .map(accountFetched -> AccountResponse.builder()
                        .userId(accountFetched.getUserId())
                        .accountNumber(accountFetched.getAccountNumber())
                        .routingNumber(accountFetched.getRoutingNumber())
                        .accountType(addAccountType(accountFetched.getAccountType()))
                        .balance(accountFetched.getBalance())
                        .accountStatus(addAccountStatus(accountFetched.getAccountStatus()))
                        .message("Fetched bank account number " + accountFetched.getAccountNumber())
                        .build())
                .toList();
    }

    public List<AccountResponse> fetchAllBankAccountsInDB(){
        log.info("Fetching all bank accounts....");
        return accountRepo.findAll()
                .stream()
                .map(accountDB -> AccountResponse.builder()
                        .userId(accountDB.getUserId())
                        .accountNumber(accountDB.getAccountNumber())
                        .routingNumber(accountDB.getRoutingNumber())
                        .accountType(addAccountType(accountDB.getAccountType()))
                        .balance(accountDB.getBalance())
                        .accountStatus(addAccountStatus(accountDB.getAccountStatus()))
                        .message("Fetched bank account number " + accountDB.getAccountNumber())
                        .build())
                .toList();
    }

    public AccountResponse updateBankAccountBalanceInDB(Transaction transaction){
        log.info("Making transaction on bank account for $" + transaction.amount() + "....");
        Account accountDB = accountRepo.findByAccountNumber(transaction.accountNumber())
                .orElseThrow(() -> new InvalidAccountException("Bank account does not exist...."));
        if (transaction.amount() <= 0)
            throw new InvalidTransactionException("Cannot make transaction of zero or negative amount....");
        else if (transaction.transactionType() == 'W' && transaction.amount() > accountDB.getBalance())
            throw new InvalidTransactionException("Cannot withdraw more than current balance....");
        if(transaction.transactionType() == 'T'){
            Account accountDB2 = accountRepo.findAllByUserId(accountDB.getUserId()).get()
                    .stream()
                    .filter(account -> !account.getAccountNumber().equals(accountDB.getAccountNumber()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidAccountException("No secondary bank account...."));
            accountDB.setBalance(accountDB.getBalance() - transaction.amount());
            accountDB2.setBalance(accountDB2.getBalance() + transaction.amount());
            accountRepo.save(accountDB2);
        } else {
            accountDB.setBalance(transaction.transactionType() == 'W'
                    ? accountDB.getBalance() - transaction.amount()
                    : accountDB.getBalance() + transaction.amount());
        }
        accountRepo.saveAndFlush(accountDB);
        return AccountResponse.builder()
                .userId(accountDB.getUserId())
                .accountNumber(accountDB.getAccountNumber())
                .routingNumber(accountDB.getRoutingNumber())
                .accountType(addAccountType(accountDB.getAccountType()))
                .balance(accountDB.getBalance())
                .accountStatus(addAccountStatus(accountDB.getAccountStatus()))
                .message("Transaction complete!")
                .build();
    }

    public AccountResponse updateBankAccountStatusInDB(AccountDTO accountDTO) {
        log.info("Changing bank account status on user id " + accountDTO.userId() + "....");
        Account accountDB = accountRepo.findByAccountNumber(accountDTO.accountNumber())
                .orElseThrow(() -> new InvalidAccountException("Bank account does not exist...."));
        if(accountDTO.accountStatus() == 'D') {
            accountRepo.delete(accountDB);
            return AccountResponse.builder()
                    .message("Bank account denied....")
                    .build();
        }
        accountDB.setAccountStatus(accountDTO.accountStatus());
        accountRepo.saveAndFlush(accountDB);
        return AccountResponse.builder()
                .userId(accountDB.getUserId())
                .accountNumber(accountDB.getAccountNumber())
                .routingNumber(accountDB.getRoutingNumber())
                .accountType(addAccountType(accountDB.getAccountType()))
                .balance(accountDB.getBalance())
                .accountStatus(addAccountStatus(accountDB.getAccountStatus()))
                .message("Bank account approved!")
                .build();
    }

    public String addAccountType(Character accountType) {
        return accountType == 'C'
                ? CHECKING
                : SAVINGS;
    }

    public String addAccountStatus(Character accountStatus){
        String activeStatus = accountStatus == 'A'
                ? APPROVED
                : PENDING;
        return accountStatus == 'D'
                ? DENIED
                : activeStatus;
    }
}