package com.bankapp.accountsmicro.controller;

import com.bankapp.accountsmicro.entity.Account;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class ApiController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/addAccount")
    public ResponseEntity<AccountResponse> saveAccount(@RequestBody Account account){
        return ResponseEntity.ok(accountService.recordAccountInDB(account));
    }

    @GetMapping("/getAccounts/{userId}")
    public ResponseEntity<List<AccountResponse>> retrieveAccountsByUserId(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(accountService.findAccountsByUserIdInDB(userId));
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<AccountResponse>> retrieveAllAccounts(){
        return ResponseEntity.ok(accountService.findAllAccountsInDB());
    }

    @PutMapping("/editAccount")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody List<String> transaction){
        return ResponseEntity.ok(accountService.updateAccountInDB(transaction));
    }
}