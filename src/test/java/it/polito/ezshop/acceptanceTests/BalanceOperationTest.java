package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import org.junit.Test;
import static org.junit.Assert.assertThrows;
import it.polito.ezshop.data.BalanceOperationClass;

import static junit.framework.TestCase.assertFalse;

public class BalanceOperationTest {
    @Test
    public void testNegativeId() throws InvalidTransactionIdException {
        BalanceOperationClass bo = new BalanceOperationClass();
        bo.setBalanceId(-2);
        assertThrows(InvalidTransactionIdException.class, () -> {bo.setBalanceId(-2);});
    }

    @Test
    public void testNullType() throws NullPointerException {
        BalanceOperationClass bo = new BalanceOperationClass();
        bo.setType(null);
        assertThrows(NullPointerException.class, () -> {bo.setType(null);});
    }

    @Test
    public void testInvalidType() throws InvalidTransactionIdException {
        BalanceOperationClass bo = new BalanceOperationClass();
        bo.setType(null);
        assertThrows(InvalidTransactionIdException.class, () -> {bo.setType(null);});
    }

}
