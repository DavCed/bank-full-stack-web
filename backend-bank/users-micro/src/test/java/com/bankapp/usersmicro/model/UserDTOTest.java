package com.bankapp.usersmicro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDTOTest {

    @Test
    void testUserDTO() {
        Integer userId = 1;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@email.com";
        String password = "password";
        String phoneNumber = "123-456-7890";
        Character userType = 'C';

        // Create a UserDTO object using the constructor
        UserDTO userDTO = new UserDTO(userId, firstName, lastName, email, password, phoneNumber, userType);

        // Verify that the fields are correctly set
        assertEquals(userId, userDTO.userId());
        assertEquals(firstName, userDTO.firstName());
        assertEquals(lastName, userDTO.lastName());
        assertEquals(email, userDTO.email());
        assertEquals(password, userDTO.password());
        assertEquals(phoneNumber, userDTO.phoneNumber());
        assertEquals(userType, userDTO.userType());

        // Verify that the Lombok-generated toString() method does not throw an exception
        assertNotNull(userDTO.toString());
    }
}