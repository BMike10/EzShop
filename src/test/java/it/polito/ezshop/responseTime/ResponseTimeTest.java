package it.polito.ezshop.responseTime;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.UnauthorizedException;;

public class ResponseTimeTest {
	private final static EZShop ezshop = new EZShop();
	private static int userId;
	
	private static String getBarcode() {
    	String barcode="";
    	Random r = new Random();
    	String nums = "0123456789";
    	for(int i=0;i<13;i++) {
    		barcode += nums.charAt(r.nextInt(10));
    	}
    	// compute the check digit
    	int digit = 0;
    	for(int i=0, j=0; i<13;i++) {
    		// add digit * (3 if even index, 1 if odd index) to result
    		digit += Integer.parseInt(""+barcode.charAt(j)) * (i%2==0 ? 3 : 1);
    		j++;
    	}
    	// compute the rounded division to get the final check digit
    	digit = ((digit + 9) / 10)*10 - digit;
    	
    	return barcode+digit;
    }
	
	@BeforeClass
	public static void init() throws Exception{
		ezshop.reset();
		userId = ezshop.createUser("testAccountResponseTime", "admin", RoleEnum.Administrator.name());
		ezshop.login("testAccountResponseTime", "admin");
		String barcode=null;
		ezshop.recordBalanceUpdate(10000);
		// populate db
		// insert 500 products
		for(int i=0;i<1500;i++) {
			do {
				barcode = getBarcode();
			}while(ezshop.getProductTypeByBarCode(barcode) != null);
			int id = ezshop.createProductType(barcode, barcode, 1.0, null);
			ezshop.updateQuantity(id, 500);
			ezshop.updatePosition(id, barcode.substring(0,5)+"-"+barcode.substring(5,10)+"-"+barcode.substring(10));			
		}
		// insert 500 sales
		for(int i=0; i<1500;i++) {
			int id = ezshop.startSaleTransaction();
			ezshop.addProductToSale(id, barcode, 10);
			ezshop.endSaleTransaction(id);
		}
		// insert 500 orders
		for(int i=0;i<1500;i++) {
			int id = ezshop.issueOrder(barcode, 10, 0.5);
			if(i%2==0)
				ezshop.payOrder(id);
			if(i%4==0)
				ezshop.recordOrderArrival(id);
		}
	}
	
	@AfterClass
	public static void after() throws Exception{
		ezshop.reset();
		if(userId > 0)
			ezshop.deleteUser(userId);
	}
	
	@Test
	public void testGetAllProducts() throws Exception{
		long start = System.currentTimeMillis();
		int size = ezshop.getAllProductTypes().size();
		long end = System.currentTimeMillis();
		assertTrue(end - start < 500);
		System.out.println("getAllProducts: "+(end-start) + " size: "+size);
	}
	
	@Test
	public void testGetAllOrders() throws Exception{
		long start = System.currentTimeMillis();
		int size = ezshop.getAllOrders().size();
		long end = System.currentTimeMillis();
		assertTrue(end - start < 500);
		System.out.println("getAllOrders: "+(end-start) + " size: "+size);
	}
}
