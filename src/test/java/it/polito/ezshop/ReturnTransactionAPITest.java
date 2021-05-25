package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.data.SaleStatus;
import it.polito.ezshop.data.SaleTransactionClass;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ReturnTransactionAPITest {
	private final EZShop ezshop = new EZShop();
	private String username = "testReturnTransactionApiEZShop";
	private String password = "password";
	private int createdUserId = -1;
	private int createdCashier = -1;
	private String usernameC = "testSaleTransactionApiUserCashier";
	private ProductType pt1 = null;
	private int newProdId1 = -1;
	private int newProdId2 = -1;
	private ProductType pt2 = null;
	private int id = -1;
	private int idC=-1;

	@Before
	public void init() throws Exception {
		User u = null;
		if ((u = ezshop.login(username, password)) == null) {
			createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
		} else if (u.getRole().equals(RoleEnum.Cashier.name())) {
			do {
				username += "1234";
				createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
			} while (createdUserId < 0);
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
		if ((pt1 = ezshop.getProductTypeByBarCode("4006381333900")) == null) {
			
			newProdId1 = ezshop.createProductType("testSaleTransactionProduct", "4006381333900", 3.5, null);
		}
		
		ezshop.updatePosition(ezshop.getProductTypeByBarCode("4006381333900").getId() , "3-ctest-3");
		ezshop.updateQuantity(newProdId1 > 0 ? newProdId1 : pt1.getId(), 5);
		//to see the quantity of the updated product
		System.out.println(ezshop.getProductTypeByBarCode("4006381333900").getQuantity());
		
		if ((pt2 = ezshop.getProductTypeByBarCode("4006381333931")) == null) {
			
			newProdId2 = ezshop.createProductType("testSaleTransactionProduct", "4006381333931", 7.0, null);
		}
		ezshop.updatePosition(ezshop.getProductTypeByBarCode("4006381333931").getId() , "3-ctest-4");
		ezshop.updateQuantity(newProdId2 > 0 ? newProdId2 : pt2.getId(), 10);
		//to see the quantity of the updated product
		System.out.println(ezshop.getProductTypeByBarCode("4006381333931").getQuantity());		
		// start and add some products to a new sale transaction
		id = ezshop.startSaleTransaction();
		ezshop.addProductToSale(id, "4006381333900", 2);
		ezshop.addProductToSale(id, "4006381333931", 1);
		SaleTransactionClass stc=(SaleTransactionClass)ezshop.getAccountBook().getSaleTransaction(id);
		stc.setStatus(SaleStatus.CLOSED);
		ezshop.logout();
	}

	@After
	public void after() throws Exception {							//id qui è usato in modo diverso rispetto agli altri metodi
		if (createdUserId > 0) {
			ezshop.login(username, password);
			// delete created product
			if (newProdId1 < 0) {
				ezshop.updateProduct(pt1.getId(), pt1.getProductDescription(), pt1.getBarCode(), pt1.getPricePerUnit(),
						pt1.getNote());
			} else
				ezshop.deleteProductType(newProdId1);
			if (newProdId2 < 0) {
				ezshop.updateProduct(pt2.getId(), pt2.getProductDescription(), pt2.getBarCode(), pt2.getPricePerUnit(),
						pt2.getNote());
			} else
				ezshop.deleteProductType(newProdId2);
			// reinsert prod
			if (pt1 != null) {
				int id = ezshop.createProductType(pt1.getProductDescription(), pt1.getBarCode(), pt1.getPricePerUnit(),
						pt1.getNote());
				ezshop.updatePosition(id, pt1.getLocation());
				ezshop.updateQuantity(id, pt1.getQuantity());
			}
			if (pt2 != null) {
				int id = ezshop.createProductType(pt2.getProductDescription(), pt2.getBarCode(), pt2.getPricePerUnit(),
						pt2.getNote());
				ezshop.updatePosition(id, pt2.getLocation());
				ezshop.updateQuantity(id, pt2.getQuantity());
			}

			ezshop.deleteUser(createdUserId);
			if (createdCashier > 0)
				ezshop.deleteUser(createdCashier);
		}
		if (id > 0) {
			ezshop.getAccountBook().removeSaleTransaction(id);
		}
	}

	@Test
	public void testStartReturnTransaction() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.startReturnTransaction(2);
		});
		// login Admin
		ezshop.login(username, password);
		// null transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.startReturnTransaction(null);
		});
		// invalid transactionId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.startReturnTransaction(-1);
		});
		// wrong transactionId
		assertEquals(-1, ezshop.startReturnTransaction(10), 0.0001);
		// try to create a return transaction on a sale that has not been payed
		SaleTransactionClass st = (SaleTransactionClass) ezshop.getSaleTransaction(id);
		st.setStatus(SaleStatus.STARTED);
		assertEquals(-1, ezshop.startReturnTransaction(st.getTicketNumber()), 0.0001);
		// change the status to payed
		st.setStatus(SaleStatus.PAYED);
		// valid
		assertTrue(ezshop.startReturnTransaction(id) != -1);

	}

	@Test
	public void testReturnProduct() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.startReturnTransaction(2);
		});
		// login Admin
		ezshop.login(username, password);
		// start a new return transaction
		int retId = ezshop.startReturnTransaction(id);
		// null returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.returnProduct(null, "4006381333900", 1);
		});
		// invalid returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.returnProduct(-1, "4006381333900", 1);
		});
		// wrong returnId
		assertEquals(false, ezshop.returnProduct(35, "4006381333900", 1));
		// null productCode
		assertThrows(InvalidProductCodeException.class, () -> {
			ezshop.returnProduct(retId, null, 1);
		});
		// invalid productCode
		assertThrows(InvalidProductCodeException.class, () -> {
			ezshop.returnProduct(retId, "", 1);
		});
		// invalid productCode
		assertThrows(InvalidProductCodeException.class, () -> {
			ezshop.returnProduct(retId, "111111111111111111", 1);
		});
		// invalid amount
		assertThrows(InvalidQuantityException.class, () -> {
			ezshop.returnProduct(retId, pt1.getBarCode(), -1);
		});
		// try to return a product that wasn't in the transaction
		assertEquals(false, ezshop.returnProduct(retId, "5006381633956", 1));

		// valid case
		// first, see how many products are there for the type we want to return, both
		// in the shop and in the relative sale transaction
		int q1 = pt1.getQuantity();
		SaleTransactionClass stc = (SaleTransactionClass) ezshop.getAccountBook().getReturnTransaction(retId)
				.getSaleTransaction();
		int q2 = stc.getProductsEntries().get(pt1.getBarCode()).getAmount();
		// return product
		ezshop.returnProduct(retId, pt1.getBarCode(), 1);
		// check that the product quantity has been updated in the shop
		assertEquals(q1 - 1, pt1.getQuantity(), 0.0001);
		// check that the quantity has been updated in the sale transaction
		assertEquals(q2 - 1, stc.getProductsEntries().get(pt1.getBarCode()).getAmount(), 0.0001);
		// undo the operation
		ezshop.addProductToSale(stc.getTicketNumber(), pt1.getBarCode(), 1);
	}

	@Test
	public void testEndReturnTransaction() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.startReturnTransaction(2);
		});
		// login Admin
		ezshop.login(username, password);
		// start a new return transaction
		int retId = ezshop.startReturnTransaction(id);
		// null returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.endReturnTransaction(null, true);
		});
		// invalid returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.endReturnTransaction(-1, true);
		});
		// wrong returnId
		assertEquals(false, ezshop.endReturnTransaction(35, true));
		// valid-->commit==true

		
		
		// valid-->commit==false

		
		
	}

	@Test
	public void testDeleteReturnTransaction() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, () -> {
			ezshop.startReturnTransaction(2);
		});
		// login Admin
		ezshop.login(username, password);
		// start a new return transaction
		int retId = ezshop.startReturnTransaction(id);
		// null returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteReturnTransaction(null);
		});
		// invalid returnId
		assertThrows(InvalidTransactionIdException.class, () -> {
			ezshop.deleteReturnTransaction(-1);
		});
		// wrong returnId
		assertEquals(false, ezshop.deleteReturnTransaction(35));
		//valid
		
		
		
	}
}