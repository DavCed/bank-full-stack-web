package com.bankapp.usersmicro.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    @Test
    void testUserEntity() {
        // Create a sample User object using the Builder pattern
        User user = User.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .password("password")
                .phoneNumber("123-456-7890")
                .userType('C')
                .build();

        // Verify that the fields are correctly set
        assertEquals(1, user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@email.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("123-456-7890", user.getPhoneNumber());
        assertEquals('C', user.getUserType());

        // Verify that the Lombok-generated toString() method does not throw an exception
        assertNotNull(user.toString());
    }
}