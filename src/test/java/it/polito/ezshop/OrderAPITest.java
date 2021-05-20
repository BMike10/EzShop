package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.OrderStatus;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class OrderAPITest {
	private final EZShop ezshop = new EZShop();
	private String username="testOrderApiUser";
	private String password = "password";
	private String usernameC="testOrderApiUserCashier";
	private int createdUserId = -1;
	private int createdCashier = -1;
	private ProductType pt = null;
	private ProductType pt2 = null;
	private int newProdId = -1;
	private int id = -1;
	
	@Before
	public void init() throws Exception{
		User u = null;
		if((u=ezshop.login(username, password))==null) {
			createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
		}else if(u.getRole().equals(RoleEnum.Cashier.name())) {
			username+="123456789101112";
			createdUserId = ezshop.createUser(username, password, RoleEnum.Administrator.name());
		}
		while(true) {
			if((u=ezshop.login(usernameC, password))==null) {
				createdCashier = ezshop.createUser(usernameC, password, RoleEnum.Cashier.name());
				if(createdCashier>0)
					break;
				else
					usernameC += "1234";
			}else if(!u.getRole().equals(RoleEnum.Cashier.name())) {
				ezshop.logout();
				usernameC+="1234";
				createdCashier = ezshop.createUser(usernameC, password, RoleEnum.Cashier.name());
				if(createdCashier > 0)
					break;
			}else
				break;
		}
		// create test products
		ezshop.login(username, password);
		if((pt = ezshop.getProductTypeByBarCode("400638133390")) == null){
			newProdId = ezshop.createProductType("testOrderProduct", "400638133390", 1.0, null);
		}
		// remove a valid prod
		pt2 = ezshop.getProductTypeByBarCode("4006381333900");
		if(pt2 != null)
			ezshop.deleteProductType(pt2.getId());
		if(id > 0)
			ezshop.getAccountBook().removeOrder(id);
		ezshop.logout();
	}
	@After
	public void after() throws Exception{
		if(createdUserId > 0) {
			ezshop.login(username, password);
			// delete created product
			if(newProdId < 0) {
				ezshop.updateProduct(pt.getId(), pt.getProductDescription(), pt.getBarCode(), pt.getPricePerUnit(), pt.getNote());
			}else
				ezshop.deleteProductType(newProdId);
			// reinsert prod
			if(pt2!=null) {
				int id = ezshop.createProductType(pt2.getProductDescription(), pt2.getBarCode(), pt2.getPricePerUnit(), pt2.getNote());
				ezshop.updatePosition(id, pt2.getLocation());
				ezshop.updateQuantity(id, pt2.getQuantity());
			}
			
			ezshop.deleteUser(createdUserId);
			if(createdCashier > 0)
				ezshop.deleteUser(createdCashier);
		}
	}
	@Test
	public void testIssueOrder() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.issueOrder("400638133390", 10, 1.0);});
		// login cashier
		ezshop.login(usernameC, password);
		// cashier not auth
		assertThrows(UnauthorizedException.class, ()->{ezshop.issueOrder("400638133390", 10, 1.0);});
		// login Admin
		ezshop.logout();
		ezshop.login(username, password);
		// invalid prod code
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.issueOrder(null, 10, 1.0);});
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.issueOrder("", 10, 1.0);});
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.issueOrder("400638133395", 10, 1.0);});
		// invalid price per unit
		assertThrows(InvalidPricePerUnitException.class, ()->{ezshop.issueOrder("400638133390", 10, 0.0);});
		// invalid quantity
		assertThrows(InvalidQuantityException.class, ()->{ezshop.issueOrder("400638133390", 0, 1.0);});
		// not present barcode
		assertEquals(Integer.valueOf(-1), ezshop.issueOrder("4006381333900", 10, 0.5));
		// valid
		id = ezshop.issueOrder("400638133390", 10, 0.5);
		assertTrue(id > 0);
		assertTrue(ezshop.getAllOrders().stream().anyMatch(o->o.getBalanceId()==id));
		// remove order
		ezshop.getAccountBook().removeOrder(id);
		id = -1;
	}
	@Test
	public void testPayOrderFor() throws Exception {
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.payOrderFor("400638133390", 10, 1.0);});
		// login cashier
		ezshop.login(usernameC, password);
		// cashier not auth
		assertThrows(UnauthorizedException.class, ()->{ezshop.payOrderFor("400638133390", 10, 1.0);});
		// login Admin
		ezshop.logout();
		ezshop.login(username, password);
		double balance = ezshop.computeBalance();
		if(balance <= 0.0) {
			ezshop.recordBalanceUpdate(10.0);
			balance = 10.0;
		}
		// invalid prod code
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.payOrderFor(null, 10, 1.0);});
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.payOrderFor("", 10, 1.0);});
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.payOrderFor("400638133395", 10, 1.0);});
		// invalid price per unit
		assertThrows(InvalidPricePerUnitException.class, ()->{ezshop.payOrderFor("400638133390", 10, 0.0);});
		// invalid quantity
		assertThrows(InvalidQuantityException.class, ()->{ezshop.payOrderFor("400638133390", 0, 1.0);});
		// not present barcode
		assertEquals(Integer.valueOf(-1), ezshop.payOrderFor("4006381333900", 10, 0.5));
		// valid not balance
		assertTrue(ezshop.payOrderFor("400638133390", 10, balance) == -1);
		assertEquals(balance, ezshop.computeBalance(), 1e-6);
		// valid
		id = ezshop.payOrderFor("400638133390", 1, balance / 5);
		assertTrue(id > 0);
		assertTrue(ezshop.getAllOrders().stream().anyMatch(o->o.getBalanceId()==id && o.getStatus().equals(OrderStatus.PAYED.name())));
		assertEquals(ezshop.computeBalance(), balance - balance / 5, 1e-3);
		// remove order
		ezshop.getAccountBook().removeOrder(id);
		id = -1;
	}
	@Test
	public void testPayOrder() {
		
	}
	@Test
	public void testRecordOrderArrival() {
		
	}
	@Test
	public void testGetAllOrders() {
		
	}
}
