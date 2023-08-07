package com.bankapp.usersmicro.service;

import static com.bankapp.usersmicro.UsersMicroApplication.encoder;
import com.bankapp.usersmicro.exceptions.ExistentUserException;
import com.bankapp.usersmicro.exceptions.NonExistentUserException;
import com.bankapp.usersmicro.model.UserDTO;
import com.bankapp.usersmicro.model.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.repo.UserRepo;
import com.bankapp.usersmicro.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepo userRepo;
    private static final String CUSTOMER = "Customer";
    private static final String EMPLOYEE = "Employee";

    public UserResponse saveUserInDB(UserDTO userDTO){
        log.info("Saving user as " + addUserType(userDTO.userType()).toLowerCase() + "....");
        User userDB = userRepo.findByEmail(userDTO.email())
                .orElseThrow(() -> new ExistentUserException("User already exists...."));
        User user = User.builder()
                .userId(userDB.getUserId())
                .firstName(userDB.getFirstName())
                .lastName(userDB.getLastName())
                .email(userDB.getEmail())
                .password(encoder.encode(userDB.getPassword()))
                .userType(userDB.getUserType())
                .phoneNumber(userDB.getPhoneNumber())
                .build();
        User userCreated = userRepo.save(user);
        return UserResponse.builder()
                .userId(userCreated.getUserId())
                .name(userCreated.getFirstName() + " " + userCreated.getLastName())
                .email(userCreated.getEmail())
                .userType(userCreated.getUserType())
                .message("User created!")
                .build();
    }

    public UserResponse fetchUserByIdInDB(Integer userId) {
        log.info("Fetching user by user id " + userId + "....");
        User userDB = userRepo.findById(userId)
                .orElseThrow(() -> new NonExistentUserException("User does not exist...."));
        return UserResponse.builder()
                .userId(userDB.getUserId())
                .name(userDB.getFirstName() + " " + userDB.getLastName())
                .email(userDB.getEmail())
                .userType(userDB.getUserType())
                .message("Fetched user id " + userId)
                .build();
    }

    public List<UserResponse> fetchAllUsersInDB(){
        log.info("Fetching all users....");
        return userRepo.findAll()
                .stream()
                .map(userDB -> UserResponse.builder()
                        .userId(userDB.getUserId())
                        .name(userDB.getFirstName() + " " + userDB.getLastName())
                        .email(userDB.getEmail())
                        .userType(userDB.getUserType())
                        .message("Fetched user id " + userDB.getUserId())
                        .build())
                .toList();
    }

    public UserResponse validateUserInDB(Credentials credentials){
        log.info("Validating user credentials to log in....");
        User userDB = userRepo.findByEmail(credentials.email())
                .orElseThrow(() -> new NonExistentUserException("User does not exist...."));
        if(!encoder.matches(credentials.password().trim(), userDB.getPassword()))
            throw new NonExistentUserException("User entered wrong password....");
        return UserResponse.builder()
                .userId(userDB.getUserId())
                .name(userDB.getFirstName() + " " + userDB.getLastName())
                .email(userDB.getEmail())
                .userType(userDB.getUserType())
                .message("Login successful!")
                .build();
    }

    public String addUserType(Character userType){
        return userType == 'C'
                ? CUSTOMER
                : EMPLOYEE;
    }
}