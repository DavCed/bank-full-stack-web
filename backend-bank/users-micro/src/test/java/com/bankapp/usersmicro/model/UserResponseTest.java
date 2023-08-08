package com.bankapp.usersmicro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserResponseTest {

    @Test
    void testUserResponse() {
        Integer userId = 1;
        String name = "John Doe";
        String email = "john.doe@email.com";
        Character userType = 'C';
        String message = "User details fetched successfully.";

        // Create a UserResponse object using the all-args constructor
        UserResponse userResponse = new UserResponse(userId, name, email, userType, message);

        // Verify that the fields are correctly set
        assertEquals(userId, userResponse.getUserId());
        assertEquals(name, userResponse.getName());
        assertEquals(email, userResponse.getEmail());
        assertEquals(userType, userResponse.getUserType());
        assertEquals(message, userResponse.getMessage());

        // Verify that the Lombok-generated toString() method does not throw an exception
        assertNotNull(userResponse.toString());
    }
}