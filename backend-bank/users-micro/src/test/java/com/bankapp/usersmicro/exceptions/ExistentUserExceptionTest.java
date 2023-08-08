package com.bankapp.usersmicro.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExistentUserExceptionTest {

    @Test
    void testExistentUserException() {
        String message = "User with email 'john.doe@email.com' already exists.";

        // Verify that the exception is thrown with the correct message
        ExistentUserException exception = assertThrows(ExistentUserException.class, () -> {
            throw new ExistentUserException(message);
        });

        assertEquals(message, exception.getMessage());
    }
}