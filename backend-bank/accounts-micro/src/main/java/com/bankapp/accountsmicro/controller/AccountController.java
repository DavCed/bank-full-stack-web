package com.bankapp.accountsmicro.controller;

import com.bankapp.accountsmicro.model.AccountDTO;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.model.Transaction;
import com.bankapp.accountsmicro.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${API_ACCOUNTS}")
@CrossOrigin(origins = "${WEB_HOST}")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/addBankAccount")
    public ResponseEntity<AccountResponse> saveBankAccount(@RequestBody AccountDTO accountDTO){
        return ResponseEntity.ok(accountService.saveBankAccountInDB(accountDTO));
    }

    @GetMapping("/getBankAccounts/{userId}")
    public ResponseEntity<List<AccountResponse>> fetchBankAccountsByUserId(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(accountService.fetchBankAccountsByUserIdInDB(userId));
    }

    @GetMapping("/getAllBankAccounts")
    public ResponseEntity<List<AccountResponse>> fetchAllBankAccounts(){
        return ResponseEntity.ok(accountService.fetchAllBankAccountsInDB());
    }

    @PutMapping("/editBankAccountBalance")
    public ResponseEntity<AccountResponse> updateBankAccountBalance(@RequestBody Transaction transaction){
        return ResponseEntity.ok(accountService.updateBankAccountBalanceInDB(transaction));
    }

    @PutMapping("/editBankAccountStatus")
    public ResponseEntity<AccountResponse> updateBankAccountStatus(@RequestBody AccountDTO accountDTO){
        return ResponseEntity.ok(accountService.updateBankAccountStatusInDB(accountDTO));
    }
}