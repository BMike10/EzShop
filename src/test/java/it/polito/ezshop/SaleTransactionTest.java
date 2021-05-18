package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.junit.*;

import it.polito.ezshop.data.SaleTransactionClass;
import it.polito.ezshop.data.LoyaltyCard;
import it.polito.ezshop.data.LoyaltyCardClass;
import it.polito.ezshop.data.ProductTypeClass;
import it.polito.ezshop.data.SaleStatus;
import it.polito.ezshop.data.TicketEntryClass;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;

public class SaleTransactionTest {
	// double price, String paymentType, Time time, SaleStatus status, LoyaltyCard
	// loyaltyCard,Integer ticketNumber, Map<String, TicketEntryClass>
	// ticketEntries, double discountRate
	@Test
	public void testSaleTransactionConstructor() {
		// invalid loyalty card
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, null, 0, new HashMap<>(), 0.0);
		});
//		// invalid discount rate (<0)
//		assertThrows(InvalidCustomerCardException.class, () -> {
//			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
//					SaleStatus.STARTED, new LoyaltyCardClass(" ", 1), 0, new HashMap<>(), -1);
//		});
//		// invalid discount rate (>1)
//		assertThrows(InvalidCustomerCardException.class, () -> {
//			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
//					SaleStatus.STARTED, new LoyaltyCardClass(" ", 1), 0, new HashMap<>(), 1.2);
//		});
		//invalid payment type
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, " ", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass(" ", 1), 0, new HashMap<>(), 1.2);
		});
		// valid
		try {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 0, new HashMap<>(), 0.1);
			assertEquals(10.0, stc.getMoney(), 0.001);
			assertEquals("CREDIT_CARD", stc.getPaymentType());
			assertEquals(new Time(System.currentTimeMillis()), stc.getTime());
			assertEquals(SaleStatus.STARTED, stc.getStatus());
			assertEquals("1234567890", stc.getLoyaltyCard().getCardCode());
			assertEquals(0, stc.getBalanceId());
			assertTrue(new HashMap<>().equals(stc.getProductsEntries()));
			assertEquals(0.1, stc.getDiscountRate(), 0.0001);
		} catch (Exception e) {
			fail();
		}
	}
																			//P.S.:loyaltyCard.length=10
	@Test
	public void testAddProductToSale() throws InvalidQuantityException {
		SaleTransactionClass stc=new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
				SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 0, new HashMap<>(), 0.1);
		//initially, the hash map shouldn't have any element
		assertTrue(stc.getProductsEntries().equals(new HashMap<>()));
		//try adding a void product
		assertFalse(stc.addProduct(null, 3));
		//try adding a product with <0 quantity
		try {
			assertFalse(stc.addProduct(new ProductTypeClass(3, "null", "1234567890000", 303.0, "notes"), -1));
		} catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		}
		//valid
		try {
			assertTrue(stc.addProduct(new ProductTypeClass(3, "null", "1234567890000", 303.0, "notes"), 10));
		} catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		}
		
	}
}
