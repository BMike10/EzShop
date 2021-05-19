package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import it.polito.ezshop.data.LoyaltyCardClass;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeClass;
import it.polito.ezshop.data.ReturnStatus;
import it.polito.ezshop.data.ReturnTransactionClass;
import it.polito.ezshop.data.SaleStatus;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.SaleTransactionClass;

//int orderId, String description, double amount, LocalDate date, String type,Map<ProductType,Integer> returned, SaleTransaction saleT, ReturnStatus retstatus

public class ReturnTransactionTest {
	@Test
	public void testReturnTransactionConstructor() throws Exception {
		// invalid orderId
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(-1, "description", 3.0, LocalDate.now(), "DEBIT",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid description
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, null, 3.0, LocalDate.now(), "DEBIT",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid amount
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", -1.0, LocalDate.now(), "DEBIT",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid date
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(-1, "description", 3.0, null, "DEBIT",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid type (null)
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), null,
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid type (wrong string)
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "wrong",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid returned map
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
					null,
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					ReturnStatus.STARTED);
		});
		// invalid saleTransaction
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
					new HashMap<>(), null, ReturnStatus.STARTED);
		});
		// invalid status
		assertThrows(Exception.class, () -> {
			ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
					new HashMap<>(),
					(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD",
							new Time(System.currentTimeMillis()), SaleStatus.STARTED,
							new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
					null);
		});
		// valid

		ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		ReturnTransactionClass rtc2 = new ReturnTransactionClass((SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1), ReturnStatus.CLOSED);
	}

	@Test
	public void testSetReturnId() throws Exception {
		final ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		assertThrows(Exception.class, () -> {
			rtc.setReturnId(-2);
		});
		assertThrows(Exception.class, () -> {
			rtc.setReturnId(null);
		});
		rtc.setReturnId(3);
		assertEquals(new Integer(3), rtc.getReturnId());
	}
	
	@Test
	public void testSetReturnedProduct() throws Exception {
		final ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		assertThrows(Exception.class, ()->{
			rtc.setReturnedProduct(null);
		});
		Map<ProductType,Integer> m=new HashMap<>();
		m.put(new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes"), 3);
		rtc.setReturnedProduct(m);
		assertEquals(m, rtc.getReturnedProduct());
	}
	
	@Test
	public void testSetSaleTransaction() throws Exception {
		final ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(9.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		assertThrows(Exception.class, ()->{
			rtc.setSaleTransaction(null);
		});
		SaleTransactionClass stc=new SaleTransactionClass(10.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
				SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1);
		rtc.setSaleTransaction(stc);
		assertTrue(stc.getBalanceId()==rtc.getSaleTransaction().getTicketNumber());
	}
	
	@Test
	public void testSetStatus() throws Exception {
		final ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(9.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		assertThrows(Exception.class, ()->{
			rtc.setStatus(null);
		});
		assertThrows(Exception.class, ()->{
			rtc.setStatus("");
		});
		rtc.setStatus("PAYED");
		assertEquals("PAYED", rtc.getStatus());
	}
	
	@Test
	public void testSetMoney() throws Exception {
		final ReturnTransactionClass rtc = new ReturnTransactionClass(1, "description", 3.0, LocalDate.now(), "DEBIT",
				new HashMap<>(),
				(SaleTransaction) new SaleTransactionClass(9.0, "CREDIT_CARD", new Time(System.currentTimeMillis()),
						SaleStatus.STARTED, new LoyaltyCardClass("1234567890", 1), 2, new HashMap<>(), 0.1),
				ReturnStatus.STARTED);
		assertThrows(Exception.class, ()->{
			rtc.setMoney(-1);
		});
		rtc.setMoney(10);
		assertEquals(10, rtc.getMoney(), 0.0001);
	}

}
