package it.polito.ezshop;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import org.junit.Test;

import java.sql.Time;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class AccountBookTest {

    @Test
    public void testInvalidRemoveSaleTransaction() {
        AccountBook aB = new AccountBookClass(0);

        //Check Throws with negative id
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.removeSaleTransaction(-1);
        });
        //Check Throws with null
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.removeSaleTransaction(null);
        });
        //Check Throws with saleTransaction that doesn't exist
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.removeSaleTransaction(500);
        });
    }
    @Test
    public void testRemoveSaleTransaction() throws InvalidTransactionIdException{
        AccountBook aB = new AccountBookClass(0);

        //Check remove if id exist in Sale Transaction (IMPLICIT TESTING)
        SaleTransactionClass sT = new SaleTransactionClass();
        Integer id = aB.addSaleTransaction(sT);
        aB.removeSaleTransaction(id);

    }

    @Test
    public void testSetBalance(){
        AccountBook aB = new AccountBookClass(0);
        assertTrue(aB.setBalance(500));
        assertFalse(aB.setBalance(-500));
    }

    @Test
    public void testInvalidGetSaleTransaction() {
        AccountBook aB = new AccountBookClass(0);

        //Check Throws with negative id
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.getSaleTransaction(-1);
        });
        //Check Throws with null
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.getSaleTransaction(null);
        });
        //Check Throws with saleTransaction that doesn't exist
        assertThrows(InvalidTransactionIdException.class, () -> {
            aB.getSaleTransaction(500);
        });
    }
    @Test
    public void testGetSaleTransaction() throws InvalidTransactionIdException{
        AccountBook aB = new AccountBookClass(0);

        //Check add saleTransaction (IMPLICIT TESTING)
        SaleTransactionClass sT = new SaleTransactionClass();
        Integer id = aB.addSaleTransaction(sT);
        aB.getSaleTransaction(id);

    }

}