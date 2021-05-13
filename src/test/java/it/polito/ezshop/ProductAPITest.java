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
}
