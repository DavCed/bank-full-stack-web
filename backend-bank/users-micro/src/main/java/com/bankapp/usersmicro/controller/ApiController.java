package com.bankapp.usersmicro.controller;

import com.bankapp.usersmicro.model.UserResponse;
import lombok.AllArgsConstructor;
import com.bankapp.usersmicro.entity.User;
import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${API_USERS}")
@CrossOrigin(origins = "${WEB_HOST}")
public class ApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/${SAVE_USER}")
    public ResponseEntity<UserResponse> saveUser(@RequestBody User user){
        return ResponseEntity.ok(userService.recordUserInDB(user));
    }

    @PostMapping("/${VALID_USER}")
    public ResponseEntity<UserResponse> checkValidity(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(userService.validateUserInDB(credentials));
    }

    @GetMapping("/${FETCH_ALL}")
    public ResponseEntity<List<UserResponse>> retrieveAllUsers(){
        return ResponseEntity.ok(userService.findAllUsersInDB());
    }

    @GetMapping("/${FETCH_USER}/{id}")
    public ResponseEntity<UserResponse> retrieveUserById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(userService.findUserInDB(id));
    }
}