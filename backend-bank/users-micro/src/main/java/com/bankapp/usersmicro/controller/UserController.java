package com.bankapp.usersmicro.controller;

import com.bankapp.usersmicro.model.UserDTO;
import com.bankapp.usersmicro.model.UserResponse;
import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${API_USERS}")
@CrossOrigin(origins = "${WEB_HOST}")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.saveUserInDB(userDTO));
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserResponse> fetchUserById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(userService.fetchUserByIdInDB(id));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> fetchAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsersInDB());
    }

    @PostMapping("/checkUser")
    public ResponseEntity<UserResponse> validateUser(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(userService.validateUserInDB(credentials));
    }
}