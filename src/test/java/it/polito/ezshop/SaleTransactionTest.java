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
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;

public class SaleTransactionTest {
	// double price, String paymentType, Time time, SaleStatus status, LoyaltyCard
	// loyaltyCard,Integer ticketNumber, Map<String, TicketEntryClass>
	// ticketEntries, double discountRate
	@Test
	public void testSaleTransactionConstructor() {
		// invalid loyalty card
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD",
					new Time(System.currentTimeMillis()), SaleStatus.STARTED, null, 2, new HashMap<>(), 0.1);
		});
		// invalid discount rate (<0)
		assertThrows(InvalidDiscountRateException.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), -1);
		});
		// invalid discount rate (>1)
		assertThrows(InvalidDiscountRateException.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 2);
		});
		// invalid payment type
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, " ", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		});
		// invalid price
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(-1, "CASH", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		});
		// invalid time
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", null, SaleStatus.STARTED,
					new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		});
		// invalid status
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
					null, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		});
		// invalid ticketEntries
		assertThrows(Exception.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, null, 0.1);
		});
		// invalid ticketNumber
		assertThrows(InvalidTransactionIdException.class, () -> {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CASH", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), -1, new HashMap<>(), 0.1);
		});
		// valid
		try {
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD",
					new Time(System.currentTimeMillis()), SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2,
					new HashMap<>(), 0.1);
			assertEquals(10.0, stc.getMoney(), 0.001);
			assertEquals("CREDIT_CARD", stc.getPaymentType());
			assertEquals(new Time(System.currentTimeMillis()), stc.getTime());
			assertEquals(SaleStatus.STARTED, stc.getStatus());
			assertEquals("1234567890", stc.getLoyaltyCard().getCardCode());
			assertEquals(new Integer(2), stc.getBalanceId());
			assertTrue(new HashMap<>().equals(stc.getProductsEntries()));
			assertEquals(0.1, stc.getDiscountRate(), 0.0001);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testAddProductToSale() throws InvalidQuantityException, Exception {
		SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
				SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 0, new HashMap<>(), 0.1);
		// initially, the hash map shouldn't have any element
		assertTrue(stc.getProductsEntries().equals(new HashMap<>()));
		// try adding a void product
		assertFalse(stc.addProduct(null, 3));
		// try adding a product with <0 quantity
		try {
			assertFalse(stc.addProduct(new ProductTypeClass(3, "null", "1234567890000", 303.0, "notes"), -1));
		} catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		}
		// valid
		try {
			assertTrue(stc.addProduct(new ProductTypeClass(3, "null", "1234567890000", 303.0, "notes"), 10));
		} catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTime() throws Exception {
		SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
				SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		assertThrows( Exception.class, () -> {
			stc.setTime(null);
		});
		stc.setTime(new Time(System.currentTimeMillis()));
		assertTrue()							//??????
	}

	@Test
	public void testStatus() throws Exception {
		SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
				SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		SaleStatus st = SaleStatus.STARTED;
		// test getter
		assertTrue(st == stc.getStatus());
		// test setter
		// invalid
		assertThrows(Exception.class, () -> {
			stc.setStatus(null);
		});
		// valid
		stc.setStatus(SaleStatus.CLOSED);
		SaleStatus st2 = SaleStatus.CLOSED;
		assertTrue(st2 == stc.getStatus());
	}

	@Test
	public void testDiscountRate() throws InvalidDiscountRateException {
		SaleTransactionClass stc;
		try {
			stc = new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
					SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
			// test getter
			assertTrue(0.1 == stc.getDiscountRate());
			// test setter
			// invalid (<0)
			assertThrows(InvalidDiscountRateException.class, () -> {
				stc.setDiscountRate(-1);
			});
			// invalid (>1)
			assertThrows(InvalidDiscountRateException.class, () -> {
				stc.setDiscountRate(1.1);
			});
			// valid
			stc.setDiscountRate(0.2);
			assertTrue(0.2 == stc.getDiscountRate());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testTicketNumber() throws InvalidTransactionIdException { // balanceId = transactionId = ticketId
		try { // cosa succede se c'è già una transazione con quell'id?
			SaleTransactionClass stc = new SaleTransactionClass(10.0, "CREDIT_CARD",
					new Time(System.currentTimeMillis()), SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2,
					new HashMap<>(), 0.1);
			Integer tNumber = 2;
			// test getter
			assertTrue(tNumber == stc.getTicketNumber());
			// invalid setter
			assertThrows(InvalidTransactionIdException.class, () -> {
				stc.setTicketNumber(null);
			});
			// invalid setter
			assertThrows(InvalidTransactionIdException.class, () -> {
				stc.setTicketNumber(-1);
			});
			// valid setter
			stc.setTicketNumber(1);
			assertTrue(stc.getTicketNumber() == 1);
		} catch (Exception e) {
			fail();
		}

	}

}
