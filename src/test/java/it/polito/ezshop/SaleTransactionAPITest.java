package it.polito.ezshop;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.Connect;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.data.SaleStatus;
import it.polito.ezshop.data.SaleTransactionClass;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.TicketEntryClass;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class SaleTransactionAPITest {

	private final EZShop ezshop = new EZShop();
	private String username = "testUserProductApiEZShop";
	private String password = "password";
	private int createdUserId = -1;
	private int createdCashier = -1;
	private String usernameC = "testSaleTransactionApiUserCashier";
	private ProductType pt = null;
	private int newProdId = -1;
	private ProductType pt2 = null;
	private int id = -1;

	@Before
	public void init() throws Exception {
		User u = null;
		if ((u = ezshop.login(username, password)) == null) {
			createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
		} else if (u.getRole().equals(RoleEnum.Cashier.name())) {
			username += "123456789101112";
			createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
		}

		while (true) {
			if ((u = ezshop.login(usernameC, password)) == null) {
				createdCashier = ezshop.createUser(usernameC, password, RoleEnum.Cashier.name());
				if (createdCashier > 0)
					break;
				else
					usernameC += "1234";
			} else if (!u.getRole().equals(RoleEnum.Cashier.name())) {
				ezshop.logout();
				usernameC += "1234";
				createdCashier = ezshop.createUser(usernameC, password, RoleEnum.Cashier.name());
				if (createdCashier > 0)
					break;
			} else
				break;
		}

		// create test products changing some digits and updating their quantity
		ezshop.login(username, password);
		if ((pt = ezshop.getProductTypeByBarCode("403638133390")) == null) {
			newProdId = ezshop.createProductType("testSaleTransactionProduct", "403638133390", 3.5, null);
		}
		ezshop.updateQuantity(newProdId, 5);

		if ((pt = ezshop.getProductTypeByBarCode("503638133390")) == null) {
			newProdId = ezshop.createProductType("testSaleTransactionProduct", "503638133390", 7.0, null);
		}
		ezshop.updateQuantity(newProdId, 10);

		ezshop.logout();
	}

	@After
	public void after() throws Exception { // non so cosa dovrei fare in after di preciso
//		if (createdUserId > 0) {
//			ezshop.login(username, password);
//			// delete created product
//			if (newProdId < 0) {
//				ezshop.updateProduct(pt.getId(), pt.getProductDescription(), pt.getBarCode(), pt.getPricePerUnit(),
//						pt.getNote());
//			} else
//				ezshop.deleteProductType(newProdId);
//			// reinsert prod
//			if (pt2 != null) {
//				int id = ezshop.createProductType(pt2.getProductDescription(), pt2.getBarCode(), pt2.getPricePerUnit(),
//						pt2.getNote());
//				ezshop.updatePosition(id, pt2.getLocation());
//				ezshop.updateQuantity(id, pt2.getQuantity());
//			}
//
//			ezshop.deleteUser(createdUserId);
//			if (createdCashier > 0)
//				ezshop.deleteUser(createdCashier);
//		}
//		if (id > 0) {
//			ezshop.getAccountBook().removeOrder(id);
//		}
	}

	@Test
	public void testStartSaleTransaction() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.startSaleTransaction();
		});
		// login Admin
		ezshop.login(username, password);
		id = ezshop.startSaleTransaction();
		// test new sale transaction id
		assertTrue(id > 0);
		// test correct upload of the transaction in the account book
		assertTrue(ezshop.getAccountBook().getSaleTransaction(id) != null);
		ezshop.getAccountBook().removeOrder(id);
		id = -1;
	}

	@Test
	public void testAddProductToSale() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.addProductToSale(1, "403638133390", 2);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSale(null, "403638133390", 2);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSale(-2, "403638133390", 2);
		});
		// null productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSale(id, null, 2);
		});
		// invalid productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSale(id, "", 2);
		});
		// invalid quantity
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.addProductToSale(id, "403638133390", -2);
		});

		int q = ezshop.getProductTypeByBarCode("403638133390").getQuantity();
		// valid
		assertTrue(ezshop.addProductToSale(id, "403638133390", 2));
		// test correctly updated quantity
		assertTrue(q == ezshop.getProductTypeByBarCode("403638133390").getQuantity() + 2);

		ezshop.deleteProductFromSale(id, "403638133390", 2);
		ezshop.logout();

	}

	@Test
	public void testDeleteProductFromSale() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.deleteProductFromSale(1, "403638133390", 2);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// add product to the new sale transaction
		ezshop.addProductToSale(id, "403638133390", 2);

		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSale(null, "403638133390", 2);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSale(-2, "403638133390", 2);
		});
		// null productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSale(id, null, 2);
		});
		// invalid productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSale(id, "", 2);
		});
		// invalid quantity
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteProductFromSale(id, "403638133390", -2);
		});

		int q = ezshop.getProductTypeByBarCode("403638133390").getQuantity();
		// valid
		assertTrue(ezshop.addProductToSale(id, "403638133390", 2));
		// test correctly updated quantity
		assertTrue(q == ezshop.getProductTypeByBarCode("403638133390").getQuantity() + 2);

		ezshop.deleteProductFromSale(id, "403638133390", 2);
		ezshop.logout();
	}

	@Test
	public void testGetSaleTransaction() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.getSaleTransaction(1);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();

		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.getSaleTransaction(null);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.getSaleTransaction(-1);
		});
		// valid
		SaleTransactionClass st = (SaleTransactionClass) ezshop.getSaleTransaction(id);
		assertTrue(st.getStatus() == SaleStatus.CLOSED);

		ezshop.logout();
	}

	@Test
	public void testApplyDiscountRateToProduct() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.applyDiscountRateToProduct(1, "403638133390", 0.2);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// add product to the new sale transaction
		ezshop.addProductToSale(id, "403638133390", 2);
		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToProduct(null, "403638133390", 0.2);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToProduct(-1, "403638133390", 0.2);
		});
		// null productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToProduct(id, null, 0.2);
		});
		// invalid productCode
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToProduct(id, "", 0.2);
		});
		// invalid discountRate
		assertThrows(InvalidDiscountRateException.class, () -> {
			ezshop.applyDiscountRateToProduct(id, "403638133390", -0.3);
		});
		// invalid discountRate
		assertThrows(InvalidDiscountRateException.class, () -> {
			ezshop.applyDiscountRateToProduct(id, "403638133390", 1.3);
		});

		// valid
		ezshop.applyDiscountRateToProduct(id, "403638133390", 0.2);
		// check if product discount is updated
		List<TicketEntry> tec = ezshop.getSaleTransaction(id).getEntries();
		double disc = 0.0;
		for (TicketEntry t : tec) {
			if (t.getBarCode() == "403638133390") {
				disc = t.getDiscountRate();
				break;
			}
		}
		assertEquals(disc, 0.2, 0.0001);

		ezshop.deleteProductFromSale(id, "403638133390", 2);
		ezshop.logout();
	}

	@Test
	public void testApplyDiscountRateToSale() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.applyDiscountRateToSale(1, 0.2);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// add product to the new sale transaction
		ezshop.addProductToSale(id, "403638133390", 2);
		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToSale(null, 0.2);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.applyDiscountRateToSale(-1, 0.2);
		});
		// invalid discountRate
		assertThrows(InvalidDiscountRateException.class, () -> {
			ezshop.applyDiscountRateToSale(id, -0.3);
		});
		// invalid discountRate
		assertThrows(InvalidDiscountRateException.class, () -> {
			ezshop.applyDiscountRateToSale(id, 1.3);
		});

		// valid
		ezshop.applyDiscountRateToSale(id, 0.2);
		// check if discount for the sale is updated
		assertEquals(ezshop.getAccountBook().getSaleTransaction(id).getDiscountRate(), 0.2, 0.0001);

		ezshop.deleteProductFromSale(id, "403638133390", 2);
		ezshop.logout();
	}
	
	@Test
	public void testComputePointsForSale() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.computePointsForSale(1);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// add product to the new sale transaction
		ezshop.addProductToSale(id, "403638133390", 2);

		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.computePointsForSale(null);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.computePointsForSale(-1);
		});
		
		//valid
		int p=ezshop.computePointsForSale(id);
		assertEquals(p, ezshop.getSaleTransaction(id).getPrice()%10, 0.0001);
		
		ezshop.deleteProductFromSale(id, "403638133390", 2);
		ezshop.logout();
	}

	@Test
	public void testEndSaleTransaction() throws Exception() {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
		ezshop.endSaleTransaction(1);
		});
		// login Admin
		ezshop.login(username, password);
		// create new transaction
		id = ezshop.startSaleTransaction();
		// add product to the new sale transaction
		ezshop.addProductToSale(id, "403638133390", 2);

		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
		ezshop.endSaleTransaction(null);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
		ezshop.endSaleTransaction(-1);
		});
				
		//valid
		assertTrue(ezshop.endSaleTransaction(id));
		
		ezshop.getAccountBook().removeSaleTransaction(st.getBalanceId());
		Connect.removeSaleTransaction(id);
		ezshop.logout();
	}

	@Test
	public void testDeleteSaleTransaction() throws Exception() {
		// before login
				assertThrows(UnauthorizedException.class, () -> {
				ezshop.endSaleTransaction(1);
				});
				// login Admin
				ezshop.login(username, password);
				// create new transaction
				id = ezshop.startSaleTransaction();
				// add product to the new sale transaction
				ezshop.addProductToSale(id, "403638133390", 2);
				ezshop.endSaleTransaction(id);
				
				// null transactionId
				assertThrows(InvalidTransactionIdException.class, () -> {
				ezshop.deleteSaleTransaction(null);
				});
				// invalid transactionId
				assertThrows(InvalidTransactionIdException.class, () -> {
				ezshop.deleteSaleTransaction(-1);
				});
				//valid
				assertTrue(ezshop.deleteSaleTransaction(id));
				ezshop.logout();
	}
	
}