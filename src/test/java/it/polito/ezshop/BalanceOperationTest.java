package it.polito.ezshop;

import it.polito.ezshop.data.OrderClass;
import it.polito.ezshop.data.OrderStatus;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import it.polito.ezshop.data.BalanceOperationClass;

import java.time.LocalDate;
import java.util.Collections;

public class BalanceOperationTest {

    @Test
    public void testConstructor() {
        // invalid money
        assertThrows(Exception.class, () -> {
            final BalanceOperationClass o = new BalanceOperationClass(0,"CREDIT");
        });
        // invalid type
        assertThrows(Exception.class, () -> {
            final BalanceOperationClass o = new BalanceOperationClass(5,"cREEDiiiiit");
        });

    }

    @Test
    public void testInvalidSetBalanceId() {
        BalanceOperationClass bo = new BalanceOperationClass();

        assertThrows(InvalidTransactionIdException.class, () -> {bo.setBalanceId(-1);});

        assertThrows(InvalidTransactionIdException.class, () -> {bo.setType(null);});

    }

    @Test
    public void testSetBalanceId()throws InvalidTransactionIdException {

        BalanceOperationClass bo = new BalanceOperationClass();

        bo.setBalanceId(10);
    }
    @Test
    public void testInvalidSetDescription() {
        BalanceOperationClass bo = new BalanceOperationClass();

        assertThrows(Exception.class, () -> {bo.setDescription(String.join("", Collections.nCopies(1000, ".")));});

        assertThrows(Exception.class, () -> {bo.setDescription(null);});

    }

    @Test
    public void testSetDescription() throws Exception {

        BalanceOperationClass bo = new BalanceOperationClass();
        bo.setDescription("ciao");
    }


}
