package com.bankapp.usersmicro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.bankapp.usersmicro.exceptions.ExistentUserException;
import com.bankapp.usersmicro.exceptions.NonExistentUserException;
import com.bankapp.usersmicro.model.Credentials;
import com.bankapp.usersmicro.model.UserDTO;
import com.bankapp.usersmicro.model.UserResponse;
import com.bankapp.usersmicro.repo.UserRepo;
import com.bankapp.usersmicro.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    void testSaveUserInDB_UserExists() {
        // Given an existing user in the database with the provided email
        String email = "john.doe@email.com";
        UserDTO userDTO = new UserDTO(null, "John", "Doe", email, "password", "123-456-7890", 'C');
        User existingUser = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password("encoded_password") // Assuming the password is already encoded in the database
                .phoneNumber("123-456-7890")
                .userType('C')
                .build();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When trying to save the user with the same email
        assertThrows(ExistentUserException.class, () -> userService.saveUserInDB(userDTO));
    }

    @Test
    void testSaveUserInDB_UserDoesNotExist() {
        // Given a new user DTO
        String email = "newuser@email.com";
        UserDTO userDTO = new UserDTO(null, "New", "User", email, "password", "987-654-3210", 'C');

        // Mock the repository to return null, indicating that the user does not exist
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // Mock the repository to return the saved user (with ID)
        User savedUser = User.builder()
                .userId(1)
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .email(userDTO.email())
                .password("encoded_password")
                .phoneNumber(userDTO.phoneNumber())
                .userType(userDTO.userType())
                .build();
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // When saving the new user
        UserResponse userResponse = userService.saveUserInDB(userDTO);

        // Then verify that the user is saved and the response contains the correct data
        assertNotNull(userResponse);
        assertEquals(1, userResponse.getUserId());
        assertEquals("New User", userResponse.getName());
        assertEquals(email, userResponse.getEmail());
        assertEquals('C', userResponse.getUserType());
        assertEquals("User created!", userResponse.getMessage());
    }


    @Test
    void testFetchUserByIdInDB_UserExists() {
        // Given an existing user in the database with the provided user ID
        Integer userId = 1;
        User existingUser = User.builder()
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .password("encoded_password")
                .phoneNumber("123-456-7890")
                .userType('C')
                .build();
        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        // When fetching the user by the user ID
        UserResponse userResponse = userService.fetchUserByIdInDB(userId);

        // Then verify that the user response contains the correct data
        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals("John Doe", userResponse.getName());
        assertEquals("john.doe@email.com", userResponse.getEmail());
        assertEquals('C', userResponse.getUserType());
        assertEquals("Fetched user id " + userId, userResponse.getMessage());
    }

    @Test
    void testFetchUserByIdInDB_UserDoesNotExist() {
        // Given a non-existing user ID
        Integer userId = 100;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        // When trying to fetch the user by the non-existing user ID
        assertThrows(NonExistentUserException.class, () -> userService.fetchUserByIdInDB(userId));
    }

    @Test
    void testFetchAllUsersInDB() {
        // Given a list of users in the database
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().userId(1).firstName("John").lastName("Doe").email("john.doe@email.com").userType('C').build());
        userList.add(User.builder().userId(2).firstName("Jane").lastName("Doe").email("jane.doe@email.com").userType('C').build());
        when(userRepo.findAll()).thenReturn(userList);

        // When fetching all users
        List<UserResponse> userResponses = userService.fetchAllUsersInDB();

        // Then verify that the user responses contain the correct data
        assertNotNull(userResponses);
        assertEquals(2, userResponses.size());
        UserResponse user1Response = userResponses.get(0);
        assertEquals(1, user1Response.getUserId());
        assertEquals("John Doe", user1Response.getName());
        assertEquals("john.doe@email.com", user1Response.getEmail());
        assertEquals('C', user1Response.getUserType());
        assertEquals("Fetched user id 1", user1Response.getMessage());

        UserResponse user2Response = userResponses.get(1);
        assertEquals(2, user2Response.getUserId());
        assertEquals("Jane Doe", user2Response.getName());
        assertEquals("jane.doe@email.com", user2Response.getEmail());
        assertEquals('C', user2Response.getUserType());
        assertEquals("Fetched user id 2", user2Response.getMessage());
    }

    @Test
    void testValidateUserInDB_UserExistsAndPasswordMatches() {
        // Given an existing user in the database with the provided email and encoded password
        String email = "john.doe@email.com";
        String password = "encoded_password";
        String encodedPassword = encoder.encode(password);
        Credentials credentials = new Credentials(email, password);

        User existingUser = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password(encodedPassword)
                .phoneNumber("123-456-7890")
                .userType('C')
                .build();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When validating the user credentials
        UserResponse userResponse = userService.validateUserInDB(credentials);

        // Then verify that the user response contains the correct data
        assertNotNull(userResponse);
        assertEquals(1, userResponse.getUserId());
        assertEquals("John Doe", userResponse.getName());
        assertEquals(email, userResponse.getEmail());
        assertEquals('C', userResponse.getUserType());
        assertEquals("Login successful!", userResponse.getMessage());
    }

    @Test
    void testValidateUserInDB_UserExistsButWrongPassword() {
        // Given an existing user in the database with the provided email and encoded password
        String email = "john.doe@email.com";
        String password = "encoded_password";
        Credentials credentials = new Credentials(email, "wrong_password");

        User existingUser = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password(password)
                .phoneNumber("123-456-7890")
                .userType('C')
                .build();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When trying to validate the user credentials with the wrong password
        assertThrows(NonExistentUserException.class, () -> userService.validateUserInDB(credentials));
    }

    @Test
    void testValidateUserInDB_UserDoesNotExist() {
        // Given a non-existing user with the provided email
        String email = "nonexistent@email.com";
        Credentials credentials = new Credentials(email, "password");

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // When trying to validate the user credentials for the non-existing user
        assertThrows(NonExistentUserException.class, () -> userService.validateUserInDB(credentials));
    }

    @Test
    void testAddUserType_AllTypes() {
        // When adding user type
        String result1 = userService.addUserType('C');
        String result2 = userService.addUserType('E');
        String result3 = userService.addUserType('X');

        // Then verify user type
        assertEquals("Customer", result1);
        assertEquals("Employee", result2);
        assertEquals("Employee", result3);
    }
}