package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static it.unibo.bank.impl.SimpleBankAccount.MANAGEMENT_FEE;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {

    private static final int DEFAULT_USER_ID = 0;
    private static final double DEFAULT_USER_BALANCE = 0.0;

    private static final double TEST_DEPOSIT_AMOUNT = 100.0;
    private static final int DEPOSIT_OPERATIONS_COUNT = 20;
    private static final double TEST_NEGATIVE_WITHDROW_AMOUNT = -200.0;

    private static final int MIN_ERROR_MESSAGE_LENGTH = 10;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    void setUp() {
        mRossi = new AccountHolder("Mario", "Rossi", DEFAULT_USER_ID);
        bankAccount = new StrictBankAccount(mRossi, DEFAULT_USER_BALANCE);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    void testInitialization() {
        assertEquals(DEFAULT_USER_BALANCE, bankAccount.getBalance());
        assertEquals(DEFAULT_USER_ID, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    void testManagementFees() {
        assertEquals(0, bankAccount.getTransactionsCount());
        for (int index = 1; index <= DEPOSIT_OPERATIONS_COUNT; index++) {
            bankAccount.deposit(mRossi.getUserID(), TEST_DEPOSIT_AMOUNT);
            assertEquals(index, bankAccount.getTransactionsCount());
            assertEquals(TEST_DEPOSIT_AMOUNT * index + DEFAULT_USER_BALANCE, bankAccount.getBalance());
        }
        bankAccount.chargeManagementFees(mRossi.getUserID());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(
            DEFAULT_USER_BALANCE + TEST_DEPOSIT_AMOUNT * DEPOSIT_OPERATIONS_COUNT 
            - MANAGEMENT_FEE - TRANSACTION_FEE * DEPOSIT_OPERATIONS_COUNT, 
            this.bankAccount.getBalance()
        );
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    void testNegativeWithdraw() {
        final Exception e = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bankAccount.withdraw(mRossi.getUserID(), TETS_NEGATIVE_WITHDROW_AMOUNT);
            }
        });
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
        assertTrue(e.getMessage().length() >= MIN_ERROR_MESSAGE_LENGTH);
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    void testWithdrawingTooMuch() {
        final Exception e = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bankAccount.withdraw(mRossi.getUserID(), TEST_DEPOSIT_AMOUNT);
            }
        });
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
        assertTrue(e.getMessage().length() >= MIN_ERROR_MESSAGE_LENGTH);
    }
}
