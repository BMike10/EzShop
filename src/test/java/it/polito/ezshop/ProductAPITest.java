package it.polito.ezshop;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ProductAPITest {

	@Test
	public void testCreateProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		final EZShop ezshop = new EZShop();
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.createProductType("apple", "400638133390", 10.0, null);});		
		// login
		ezshop.login("admin2", "admin");
		// description
		// null
		assertThrows(InvalidProductDescriptionException.class, ()->{ezshop.createProductType(null, "400638133390", 10.0, null);});
		// empty
		assertThrows(InvalidProductDescriptionException.class, ()->{ezshop.createProductType("", "400638133390", 10.0, null);});
		// barcode
		// null
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.createProductType("apple", null, 10.0, null);});
		// invalid
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.createProductType("apple", "", 10.0, null);});
		// unitPrice
		// invalid
		assertThrows(InvalidPricePerUnitException.class, ()->{ezshop.createProductType("apple", "400638133390", -10.0, null);});
		// valid
		int id =-1;
		try{
			if((id=ezshop.createProductType("apple", "400638133390", 10.0, "apple"))<=0)
				fail();
		}catch(Exception e) {
			fail();
		}
		// double add
		assertEquals(new Integer(-1), ezshop.createProductType("apple", "400638133390", 10.0, "apple"));
		
		// remove
		ezshop.deleteProductType(id);
	}
	
	@Test
	public void testDeleteProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, UnauthorizedException {
		final EZShop ezshop = new EZShop();
		// login
		ezshop.login("admin2", "admin");
		int id[] = {-1};
		try{
			if((id[0]=ezshop.createProductType("apple", "400638133390", 10.0, "apple"))<=0)
				fail();
		}catch(Exception e) {
			fail();
		}
		ezshop.logout();
		
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.deleteProductType(id[0]);});	
		// login
		ezshop.login("admin2", "admin");
		// invalid id
		assertThrows(InvalidProductIdException.class, ()->{ezshop.deleteProductType(0);});	
		// valid
		assertEquals(true, ezshop.deleteProductType(id[0]));	
		// double remove
		assertFalse(ezshop.deleteProductType(id[0]));	
	}
	@Test
	public void testGetAllProductTypes() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		final EZShop ezshop = new EZShop();
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.getAllProductTypes();});	
		// login
		ezshop.login("admin2", "admin");
		// valid
		int num = -1;
		try {
			List<ProductType> prods = ezshop.getAllProductTypes();
			num = prods.size();
		}catch(Exception e) {fail();}
		// add and see if the prod is added
		final int id = ezshop.createProductType("apple", "400638133390", 10.0, "apple");
		boolean present=false;
		try {
			List<ProductType> prods = ezshop.getAllProductTypes();
			assertEquals(num+1, prods.size());
			// check if contains
			for(ProductType pt:prods) {
				if(pt.getBarCode().equals("400638133390"))
					present = true;
			}
			if(!present)
				fail();
		}catch(Exception e) {fail();}
		// remove and see if really removed
		ezshop.deleteProductType(id);
		present = false;
		try {
			List<ProductType> prods = ezshop.getAllProductTypes();
			assertEquals(num, prods.size());
			// check if contains
			for(ProductType pt:prods) {
				if(pt.getBarCode().equals("400638133390"))
					present = true;
			}
			if(present)
				fail();
		}catch(Exception e) {fail();}
	}
	@Test
	public void testGetProductTypeByBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, UnauthorizedException, InvalidProductIdException {
		final EZShop ezshop = new EZShop();
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.getProductTypeByBarCode("4006381333900");});	
		// login
		ezshop.login("admin2", "admin");
		// invalid barcode
		// null
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.getProductTypeByBarCode(null);});	
		// empty
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.getProductTypeByBarCode("");});	
		// not valid
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.getProductTypeByBarCode("4006381333903");});
		// valid but not present
		assertNull(ezshop.getProductTypeByBarCode("400638133390"));
		// present
		int id=-1;
		try {
			id = ezshop.createProductType("apple", "400638133390", 10.0, "apple");
			ProductType pt = ezshop.getProductTypeByBarCode("400638133390");
			// cehck if same prod inserted
			assertEquals(new Integer(id), pt.getId());
			assertEquals("apple", pt.getProductDescription());
			assertEquals("400638133390", pt.getBarCode());
			assertEquals(10, pt.getPricePerUnit(), 1e-6);
		}catch(Exception e) {fail();}
		// remove product
		ezshop.deleteProductType(id);
	}
	@Test
	public void testGetProductTypesByDescription() throws InvalidUsernameException, InvalidPasswordException {
		final EZShop ezshop = new EZShop();
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.getProductTypesByDescription("banana");});	
		// login
		ezshop.login("admin2", "admin");
		try {
			int id = ezshop.createProductType("apple", "400638133390", 10.0, "apple");
			// list for comparison
			List<ProductType> allProd = ezshop.getAllProductTypes();
			// null string
			List<ProductType> selProd = ezshop.getProductTypesByDescription(null);
			if(!allProd.containsAll(selProd))
				fail();
			// empty string
			selProd = ezshop.getProductTypesByDescription("");
			if(!allProd.containsAll(selProd))
				fail();
			// not present
			selProd = ezshop.getProductTypesByDescription("cdfg");
			if(selProd == null || selProd.size()>=1)
				fail();
			// only one
			selProd = ezshop.getProductTypesByDescription("pl");	// should return apple only
			if(selProd.size() != 1 || !selProd.get(0).getBarCode().equals("400638133390"))
				fail();
			// clean
			ezshop.deleteProductType(id);
		}catch(Exception e) {fail();}
	}
	@Test
	public void testUpdateProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		final EZShop ezshop = new EZShop();
		// before login
		assertThrows(UnauthorizedException.class, ()->{ezshop.updateProduct(1, "banana", "4006381333900", 10.0, null);});	
		// login
		ezshop.login("admin2", "admin");
		// create test prod
		final int id = ezshop.createProductType("apple", "400638133390", 10.0, "apple");
		ProductType pt = ezshop.getProductTypeByBarCode("400638133390");
		pt.setLocation("1-a-1");
		pt.setQuantity(10);
		// invalid id
		assertThrows(InvalidProductIdException.class, ()->{ezshop.updateProduct(0, "apple", "400638133390", 10.0, "apple");});	
		// not present id
		assertFalse(ezshop.updateProduct(Integer.MAX_VALUE, "apple", "400638133390", 10.0, "apple"));	
		// invalid description
		// null
		assertThrows(InvalidProductDescriptionException.class, ()->{ezshop.updateProduct(id, null, "400638133390", 10.0, "apple");});	
		// empty
		assertThrows(InvalidProductDescriptionException.class, ()->{ezshop.updateProduct(id, "", "400638133390", 10.0, "apple");});	
		// invalid barcode
		// null
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.updateProduct(id, "apple", null, 10.0, "apple");});	
		//empty
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.updateProduct(id, "apple", "", 10.0, "apple");});	
		//invalid
		assertThrows(InvalidProductCodeException.class, ()->{ezshop.updateProduct(id, "apple", "400638133399", 10.0, "apple");});	
		//already present
		assertFalse(ezshop.updateProduct(id, "apple", "4006381333900", 10.0, "apple"));	
		// invalid price
		assertThrows(InvalidPricePerUnitException.class, ()->{ezshop.updateProduct(id, "apple", "400638133390", 0.0, "apple");});	
		// valid
		try {
			if(!ezshop.updateProduct(id, "apple2", "400638133390", 20.0, "apple2"))
				fail();
			pt = ezshop.getProductTypeByBarCode("400638133390");
			assertEquals(new Integer(id), pt.getId());
			assertEquals("apple2", pt.getProductDescription());
			assertEquals(20.0, pt.getPricePerUnit(), 1e-6);
			assertEquals("apple2", pt.getNote());
			assertEquals(new Integer(10), pt.getQuantity());
			assertEquals("1-a-1", pt.getLocation());
			// clean
			ezshop.deleteProductType(id);
		}catch(Exception e) {fail();}
		
	}
}
