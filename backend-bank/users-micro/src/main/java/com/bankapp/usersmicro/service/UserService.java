package com.bankapp.usersmicro.service;

import com.bankapp.usersmicro.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.repo.UserRepo;
import com.bankapp.usersmicro.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserResponse recordUserInDB(User user){
        String userType = user.getUserType() == 'C' ? "Customer" : "Employee";
        log.info("Attempting to save " + user.getFirstName() + " " + user.getLastName() + " in system....");
        int count = (int) userRepo.findAll().stream()
                .filter(userInDB -> userInDB.getEmail().equals(user.getEmail())
                        | (userInDB.getFirstName() + " " + userInDB.getLastName())
                        .equals(user.getFirstName() + " " + user.getLastName()))
                .count();
        String message;
        if(count == 0){
            userRepo.save(user);
            message = "Account opened!";
        } else {
            message = "Account already exists.";
        }
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .userId(user.getId())
                .userType(user.getUserType())
                .message(message)
                .build();
    }

    public UserResponse validateUserInDB(Credentials credentials){
        StringBuilder password = new StringBuilder();
        StringBuilder email = new StringBuilder();
        Integer userId = 0;
        log.info("Attempting to log user in with email: " + credentials.getEmail() + " and password: " + credentials.getPassword());
        Optional<User> user = userRepo.findByEmail(credentials.getEmail());
        if(credentials.getEmail().length() > 0 && credentials.getPassword().length() > 0 & user.isPresent()) {
            password.append(user.get().getPassword());
            email.append(user.get().getEmail());
            userId = user.get().getId();
        }
        String message = email.toString().equals(credentials.getEmail()) & password.toString().equals(credentials.getPassword()) ?
                "Login successful!" : "Account does not exist.";
        return UserResponse.builder()
                .userId(userId)
                .email(credentials.getEmail())
                .message(message)
                .build();
    }

    public List<UserResponse> findAllUsersInDB(){
        log.info("Fetching all users in system....");
        return userRepo.findAll().stream()
                .map(user ->
                        UserResponse.builder()
                                .userId(user.getId())
                                .name(user.getFirstName() + " " + user.getLastName())
                                .userType(user.getUserType())
                                .email(user.getEmail())
                                .message("All users retrieved.")
                                .build())
                .collect(Collectors.toList());
    }

    public UserResponse findUserInDB(Integer id) {
        log.info("Searching for user by ID " + id + "....");
        User user = userRepo.findById(id).get();
        return UserResponse.builder()
                .userId(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .userType(user.getUserType())
                .email(user.getEmail())
                .message("User retrieved by id " + id)
                .build();
    }
}