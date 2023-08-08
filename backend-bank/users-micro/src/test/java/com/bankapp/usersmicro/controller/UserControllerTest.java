package com.bankapp.usersmicro.controller;

import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.model.UserDTO;
import com.bankapp.usersmicro.model.UserResponse;
import com.bankapp.usersmicro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        // Initialize the controller and inject the mock UserService
        userController = new UserController();
        userController.userService = userService;
    }

    @Test
    void testSaveUser() {
        // Create a sample UserDTO for testing
        UserDTO userDTO = new UserDTO(1,"John","Doe","john.doe@email.com","password",null,'E');
        // Assuming the saveUserInDB method returns a UserResponse object
        UserResponse userResponse = new UserResponse(1,"John","john.doe@email.com",'E',"Created");
        when(userService.saveUserInDB(userDTO)).thenReturn(userResponse);

        ResponseEntity<UserResponse> responseEntity = userController.saveUser(userDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
        verify(userService, times(1)).saveUserInDB(userDTO);
    }

    @Test
    void testFetchUserById() {
        int userId = 123;
        // Assuming the fetchUserByIdInDB method returns a UserResponse object
        UserResponse userResponse = new UserResponse(userId,"John","john.doe@email.com",'E',"Fetched");
        when(userService.fetchUserByIdInDB(userId)).thenReturn(userResponse);

        ResponseEntity<UserResponse> responseEntity = userController.fetchUserById(userId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
        verify(userService, times(1)).fetchUserByIdInDB(userId);
    }

    @Test
    void testFetchAllUsers() {
        // Assuming the fetchAllUsersInDB method returns a List<UserResponse>
        List<UserResponse> userList = new ArrayList<>();
        // Add some UserResponse objects to the userList
        UserResponse userResponse1 = new UserResponse(1,"John","john.doe@email.com",'E',"Fetched");
        UserResponse userResponse2 = new UserResponse(2,"Jane","jane.doe@email.com",'C',"Fetched");
        userList.add(userResponse1);
        userList.add(userResponse2);
        when(userService.fetchAllUsersInDB()).thenReturn(userList);

        ResponseEntity<List<UserResponse>> responseEntity = userController.fetchAllUsers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
        verify(userService, times(1)).fetchAllUsersInDB();
    }

    @Test
    void testValidateUser() {
        Credentials credentials = new Credentials("john.doe@email.com","password");
        // Assuming the validateUserInDB method returns a UserResponse object
        UserResponse userResponse = new UserResponse(1,"John","john.doe@email.com",'C',"Validated");
        when(userService.validateUserInDB(credentials)).thenReturn(userResponse);

        ResponseEntity<UserResponse> responseEntity = userController.validateUser(credentials);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userResponse, responseEntity.getBody());
        verify(userService, times(1)).validateUserInDB(credentials);
    }
}