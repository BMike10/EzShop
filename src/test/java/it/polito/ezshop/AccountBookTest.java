package it.polito.ezshop;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import org.junit.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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
        Connect.connect();
        AccountBook aB = new AccountBookClass(0);
        //Check remove if id exist in Sale Transaction (IMPLICIT TESTING)
        Map<String,TicketEntryClass> tec = new HashMap<>();
        SaleTransactionClass sT = new SaleTransactionClass(1," String description", 20.0, LocalDate.now(), "CREDIT", "CASH", Time.valueOf(LocalTime.now()),
                SaleStatus.valueOf("STARTED"),  new LoyaltyCardClass("gdskj4679v",50), tec, 5.0);
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
        assertThrows(Exception.class, () -> {
            aB.getSaleTransaction(-1);
        });
        //Check Throws with null
        assertThrows(Exception.class, () -> {
            aB.getSaleTransaction(null);
        });
        //Check Throws with saleTransaction that doesn't exist
        assertThrows(Exception.class, () -> {
            aB.getSaleTransaction(500);
        });
    }
    @Test
    public void testGetSaleTransaction() throws InvalidTransactionIdException{
        AccountBook aB = new AccountBookClass(0);
        Map<String,TicketEntryClass> tec = new HashMap<>();
        //Check add saleTransaction (IMPLICIT TESTING)
        SaleTransactionClass sT = new SaleTransactionClass(1," String description", 20.0, LocalDate.now(), "CREDIT", "CASH", Time.valueOf(LocalTime.now()),
                SaleStatus.valueOf("STARTED"),  new LoyaltyCardClass("gdskj4679v",50), tec, 5.0);
        Integer id = aB.addSaleTransaction(sT);
        aB.getSaleTransaction(id);

    }

    @Test
    public void testWhiteBoxAccountBook() throws Exception{

        AccountBookClass ab = new AccountBookClass(-25);
        assertEquals(new Double(0),ab.getBalance());

        // setSaleTransaction
        Map<Integer, SaleTransaction > sales = new HashMap<>();
        sales.put(1, new SaleTransactionClass(10.0, "CREDIT_CARD",
					new Time(System.currentTimeMillis()), SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2,
					new HashMap<>(), 0.1));
        assertThrows(Exception.class, ()->{ab.setSaleTransactionMap(null);});
        // valid
        ab.setSaleTransactionMap(sales);
        assertEquals(1, ab.getSaleTransactionMap().size());
        

        // setReturnTransaction
        Map<Integer, ReturnTransaction > returns = new HashMap<>();
        returns.put(1, new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED));
        assertThrows(Exception.class, ()->{ab.setReturnTransactionMap(null);});
        // valid
        ab.setReturnTransactionMap(returns);
        assertEquals(1, ab.getReturnTransactionMap().size());
        
        // setOrderClass
        Map<Integer, Order> orders = new HashMap<>();
        orders.put(1, new OrderClass(1, LocalDate.now(), null, "4006381333931", 1, 10, OrderStatus.ISSUED));

        assertThrows(Exception.class, ()->{ab.setOrderMap(null);});
        // valid
        ab.setOrderMap(orders);
        assertEquals(1, ab.getOrderMap().size());
        
        // addOrder
        int id = ab.addOrder(new OrderClass(2, LocalDate.now(), null, "4006381333931", 1, 10, OrderStatus.ISSUED));
        // get order
        assertThrows(Exception.class, ()->{ab.getOrder(null);});
        assertTrue(ab.getOrder(id)!=null);
        // remove order
        ab.removeOrder(id);
        
        // addSale
        id = ab.addSaleTransaction(new SaleTransactionClass(10.0, "CREDIT_CARD",
					new Time(System.currentTimeMillis()), SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2,
					new HashMap<>(), 0.1));
        // get sale
        assertThrows(Exception.class, ()->{ab.getSaleTransaction(null);});
        assertTrue(ab.getSaleTransaction(id)!=null);
        // remove sale
        ab.removeSaleTransaction(id);
        
        // add return
        id = ab.addReturnTransaction(new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED));
        //get return
        assertThrows(Exception.class, ()->{ab.getReturnTransaction(null);});
        assertTrue(ab.getReturnTransaction(id)!=null);        
        // remove return
        ab.removeReturnTransaction(id);
        
        // get balanceOperation
        assertTrue(ab.getBalanceOperationMap()!=null);
    }

}
