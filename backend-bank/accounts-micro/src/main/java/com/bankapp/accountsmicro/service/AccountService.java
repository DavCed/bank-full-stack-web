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
    private static final String APPROVED = "Approved";
    private static final String DENIED = "Denied";
    private static final String PENDING = "Pending";
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
                .accountStatus(accountDTO.accountStatus())
                .build();
        int accNum = rand.nextInt(999999999) + 1234567890;
        account.setAccountNumber(accNum < 0 ? -(accNum) : accNum);
        account.setRoutingNumber(9876543210L);
        account.setAccountStatus('P');
        Account accountDB = accountRepo.save(account);
        return AccountResponse.builder()
                .userId(accountDB.getUserId())
                .accountNumber(accountDB.getAccountNumber())
                .routingNumber(accountDB.getRoutingNumber())
                .accountType(addAccountType(accountDB.getAccountType()))
                .balance(accountDB.getBalance())
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
        accountDB.setBalance(transaction.transactionType() == 'W'
                ? accountDB.getBalance() - transaction.amount()
                : accountDB.getBalance() + transaction.amount());
        Account accountUpdated = accountRepo.save(accountDB);
        return AccountResponse.builder()
                .userId(accountUpdated.getUserId())
                .accountNumber(accountUpdated.getAccountNumber())
                .routingNumber(accountUpdated.getRoutingNumber())
                .accountType(addAccountType(accountUpdated.getAccountType()))
                .balance(accountUpdated.getBalance())
                .accountStatus(addAccountStatus(accountUpdated.getAccountStatus()))
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
        Account accountUpdated = accountRepo.save(accountDB);
        return AccountResponse.builder()
                .userId(accountUpdated.getUserId())
                .accountNumber(accountUpdated.getAccountNumber())
                .routingNumber(accountUpdated.getRoutingNumber())
                .accountType(addAccountType(accountUpdated.getAccountType()))
                .balance(accountUpdated.getBalance())
                .accountStatus(addAccountStatus(accountUpdated.getAccountStatus()))
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