package com.bankapp.accountsmicro.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidAccountExceptionTest {

    @Test
    void testInvalidAccountException() {
        String expectedMessage = "This is an invalid account.";

        try {
            throw new InvalidAccountException(expectedMessage);
        } catch (InvalidAccountException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}