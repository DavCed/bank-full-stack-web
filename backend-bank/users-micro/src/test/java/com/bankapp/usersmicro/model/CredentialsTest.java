package com.bankapp.usersmicro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CredentialsTest {

    @Test
    void testCredentials() {
        String email = "john.doe@email.com";
        String password = "password";

        // Create a Credentials object using the constructor
        Credentials credentials = new Credentials(email, password);

        // Verify that the fields are correctly set
        assertEquals(email, credentials.email());
        assertEquals(password, credentials.password());

        // Verify that the Lombok-generated toString() method does not throw an exception
        assertNotNull(credentials.toString());
    }
}