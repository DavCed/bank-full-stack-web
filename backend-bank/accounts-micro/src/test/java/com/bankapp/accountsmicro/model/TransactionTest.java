package com.bankapp.accountsmicro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void testTransactionConstructorAndGetters() {
        Integer amount = 100;
        Integer accountNumber = 12345;
        Character transactionType = 'D';

        Transaction transaction = new Transaction(amount, accountNumber, transactionType);

        assertEquals(amount, transaction.amount());
        assertEquals(accountNumber, transaction.accountNumber());
        assertEquals(transactionType, transaction.transactionType());
    }

    @Test
    void testTransactionEqualsAndHashCode() {
        Transaction transaction1 = new Transaction(100, 12345, 'D');
        Transaction transaction2 = new Transaction(100, 12345, 'D');

        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }
}