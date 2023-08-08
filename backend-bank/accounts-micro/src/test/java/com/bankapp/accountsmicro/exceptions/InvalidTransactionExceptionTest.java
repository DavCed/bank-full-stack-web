package com.bankapp.accountsmicro.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidTransactionExceptionTest {

    @Test
    void testInvalidTransactionException() {
        String expectedMessage = "This is an invalid transaction.";

        try {
            throw new InvalidTransactionException(expectedMessage);
        } catch (InvalidTransactionException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}