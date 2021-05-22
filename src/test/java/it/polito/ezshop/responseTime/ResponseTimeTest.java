package it.polito.ezshop.responseTime;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.RoleEnum;

public class ResponseTimeTest {
	private final static EZShop ezshop = new EZShop();
	private static int userId;
	private final static int N = 1500;
	
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
		ezshop.recordBalanceUpdate(10000);
	}
	
	@AfterClass
	public static void after() throws Exception{
		ezshop.reset();
		if(userId > 0)
			ezshop.deleteUser(userId);
	}
	
	@Test
	public void testProducts() throws Exception{
		String barcode = "";
		List<String> codes=new ArrayList<>();
		double create=0, getBarcode=0, updQty=0, updPos=0, getDesc=0;
		long t1;
		for(int i=0;i<N;i++) {
			do {
				barcode = getBarcode();
			}while(ezshop.getProductTypeByBarCode(barcode) != null);
			// create
			t1=System.currentTimeMillis();
			int id = ezshop.createProductType(barcode, barcode, 1.0, null);
			create += System.currentTimeMillis()-t1;
			// update quantity
			t1=System.currentTimeMillis();
			ezshop.updateQuantity(id, 500);
			updQty += System.currentTimeMillis() - t1;
			// update posiiton
			t1 = System.currentTimeMillis();
			ezshop.updatePosition(id, barcode.substring(0,5)+"-"+barcode.substring(5,10)+"-"+barcode.substring(10));	
			updPos+=System.currentTimeMillis() - t1;
			// get by barcode
			t1 = System.currentTimeMillis();
			ezshop.getProductTypeByBarCode(barcode);
			getBarcode += System.currentTimeMillis() - t1;
			// get by description
			t1=System.currentTimeMillis();
			ezshop.getProductTypesByDescription(barcode);
			getDesc += System.currentTimeMillis() - t1;
		}
		assertTrue(500 >= create / N);
		assertTrue(500 >= getBarcode / N);
		assertTrue(500 >= updQty / N);
		assertTrue(500 >= updPos / N);
		assertTrue(500 >= getDesc / N);
		long start = System.currentTimeMillis();
		int size = ezshop.getAllProductTypes().size();
		long end = System.currentTimeMillis();
		assertTrue(end - start < 500);
		System.out.println("getAllProducts: "+(end-start) + " size: "+size);
		// delete
		double delete=0;
		for(String code:codes) {
			ProductType pt = ezshop.getProductTypeByBarCode(code);
			int id = pt.getId();
			t1=System.currentTimeMillis();
			ezshop.deleteProductType(id);
			delete += System.currentTimeMillis()-t1;
		}
		assertTrue(500 >= delete / N);
	}
	
	@Test
	public void testOrders() throws Exception{
		String barcode= null;
		double issue=0, issuePay=0, pay=0, arrive=0;
		long t;
		do {
			barcode = getBarcode();
		}while(ezshop.getProductTypeByBarCode(barcode) != null);
		int pid = ezshop.createProductType(barcode, barcode, 1.0, null);
		ezshop.updatePosition(pid, barcode.substring(0,5)+"-"+barcode.substring(5, 10)+"-"+barcode.substring(10));
		// insert money
		ezshop.recordBalanceUpdate(N*6*2);
		// insert 1500 orders
		for(int i=0;i<N;i++) {
			// issue
			t=System.currentTimeMillis();
			int id = ezshop.issueOrder(barcode, 10, 0.5);
			issue+=System.currentTimeMillis() - t;
			// pay
			t=System.currentTimeMillis();
			ezshop.payOrder(id);
			pay+=System.currentTimeMillis()-t;
			// record arrival
			t=System.currentTimeMillis();
			ezshop.recordOrderArrival(id);
			arrive+=System.currentTimeMillis()-t;
			// issue & pay
			t=System.currentTimeMillis();
			ezshop.payOrderFor(barcode, 10, 0.5);
			issuePay+=System.currentTimeMillis()-t;
		}
		assertTrue(500 >= issue / N);
		assertTrue(500 >= issuePay / N);
		assertTrue(500 >= pay / N);
		assertTrue(500 >= arrive / N);
		long start = System.currentTimeMillis();
		int size = ezshop.getAllOrders().size();
		long end = System.currentTimeMillis();
		assertTrue(end - start < 500);
		System.out.println("getAllOrders: "+(end-start) + " size: "+size);
	}
	
	@Test
	public void testSale() throws Exception {
		List<String> barcodes = new ArrayList<>();
		String barcode;
		double start=0, add=0, end=0, delete=0, discountP=0, discountS=0, points=0, deleteS=0, get=0;
		long t;
		for(int i=0;i<10;i++) {
			do {
				barcode = getBarcode();
			}while(ezshop.getProductTypeByBarCode(barcode) != null);
			int pid = ezshop.createProductType(barcode, barcode, 1.0, null);
			barcodes.add(barcode);
		}
		// insert 1500 sales
		for(int i=0; i<N;i++) {
			// start
			t=System.currentTimeMillis();
			int id = ezshop.startSaleTransaction();
			start += System.currentTimeMillis() - t;
			// add product
			t=System.currentTimeMillis();
			ezshop.addProductToSale(id, barcodes.get(0), 10);
			add += System.currentTimeMillis()-t;
			// apply discount
			t=System.currentTimeMillis();
			ezshop.applyDiscountRateToProduct(id, barcodes.get(0), 0.1);
			discountP+=System.currentTimeMillis()-t;
			// add other prod
			for(int j=1;j<10;j++)
				ezshop.addProductToSale(id, barcodes.get(j), 2);
			// delete product
			t=System.currentTimeMillis();
			ezshop.deleteProductFromSale(id, barcodes.get(0), 1);
			delete += System.currentTimeMillis() -t;
			// discount sale
			t=System.currentTimeMillis();
			ezshop.applyDiscountRateToSale(id, 0.1);
			discountS += System.currentTimeMillis()-t;
			// compute points
			t=System.currentTimeMillis();
			ezshop.computePointsForSale(id);
			points += System.currentTimeMillis() - t;
			// end sale
			t=System.currentTimeMillis();
			ezshop.endSaleTransaction(id);
			end += System.currentTimeMillis()-t;
			// get
			t=System.currentTimeMillis();
			ezshop.getSaleTransaction(id);
			get += System.currentTimeMillis()-t;
		}
		assertTrue(500 >= start / N);
		assertTrue(500 >= add / N);
		assertTrue(500 >= discountP / N);
		assertTrue(500 >= delete / N);
		assertTrue(500 >= discountS / N);
		assertTrue(500 >= end / N);
		assertTrue(500 >= get / N);
		assertTrue(500 >= points / N);
	}
}
