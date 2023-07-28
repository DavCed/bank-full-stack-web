package com.bankapp.accountsmicro.controller;

import com.bankapp.accountsmicro.entity.Account;
import com.bankapp.accountsmicro.model.AccountResponse;
import com.bankapp.accountsmicro.model.Transaction;
import com.bankapp.accountsmicro.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${API_ACCOUNTS}")
@CrossOrigin(origins = "${WEB_HOST}")
public class ApiController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/${SAVE_ACC}")
    public ResponseEntity<AccountResponse> saveAccount(@RequestBody Account account){
        return ResponseEntity.ok(accountService.recordAccountInDB(account));
    }

    @GetMapping("/${FETCH_ACC}/{userId}")
    public ResponseEntity<List<AccountResponse>> retrieveAccountsByUserId(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(accountService.findAccountsByUserIdInDB(userId));
    }

    @GetMapping("/${FETCH_ALL}")
    public ResponseEntity<List<AccountResponse>> retrieveAllAccounts(){
        return ResponseEntity.ok(accountService.findAllAccountsInDB());
    }

    @PutMapping("/${CHANGE_ACC}")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody Transaction transaction){
        return ResponseEntity.ok(accountService.updateAccountInDB(transaction));
    }
}