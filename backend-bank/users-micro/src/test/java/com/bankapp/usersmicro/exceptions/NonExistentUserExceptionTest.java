package com.bankapp.usersmicro.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NonExistentUserExceptionTest {

    @Test
    void testNonExistentUserException() {
        String message = "User with ID 1 does not exist.";

        // Verify that the exception is thrown with the correct message
        NonExistentUserException exception = assertThrows(NonExistentUserException.class, () -> {
            throw new NonExistentUserException(message);
        });

        assertEquals(message, exception.getMessage());
    }
}