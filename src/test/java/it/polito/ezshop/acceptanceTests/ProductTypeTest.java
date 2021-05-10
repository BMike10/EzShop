package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.Position;
import it.polito.ezshop.data.ProductTypeClass;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;

public class ProductTypeTest {
	@Test
	public void testValidateBarCode() {
		// ivalid 13
		String str = "4006381333939";
		assertFalse(ProductTypeClass.validateBarCode(str));
		// valid 13
		str = "4006381333931";
		assertTrue(ProductTypeClass.validateBarCode(str));
		// null
		assertFalse(ProductTypeClass.validateBarCode(null));
		// empty
		assertFalse(ProductTypeClass.validateBarCode(""));
		// not digit
		assertFalse(ProductTypeClass.validateBarCode("asdasdasdsdas"));
		// valid 12
		str = "400638133390";
		assertTrue(ProductTypeClass.validateBarCode(str));
		// invalid 12
		str = "400638133396";
		assertFalse(ProductTypeClass.validateBarCode(str));
		// valid 14
		str = "4006381333900";
		assertTrue(ProductTypeClass.validateBarCode(str));
		// invalid 14
		str = "4006381333905";
		assertFalse(ProductTypeClass.validateBarCode(str));
		// with spaces
		str = " 400 638 133 3900 ";
		assertTrue(ProductTypeClass.validateBarCode(str));
		str = " 400 638 133 3901 ";
		assertFalse(ProductTypeClass.validateBarCode(str));
		assertFalse(ProductTypeClass.validateBarCode("             "));
		// invalid 11
		str = "4006381333";
		assertFalse(ProductTypeClass.validateBarCode(str));
		// invalid 15
		str = "40063813339054";
		assertFalse(ProductTypeClass.validateBarCode(str));
	}
	@Test
	public void testProductTypeConstructor() {
		ProductTypeClass pt;
		// invalid description
		try {
			pt = new ProductTypeClass(1, null, "4006381333900", 1.0, null);
			fail();
		}catch(InvalidProductDescriptionException e1) {}
		catch(Exception e) {fail();}
		// invalid id
		try {
			pt = new ProductTypeClass(0, "null", "4006381333900", 1.0, null);
			fail();
			}
		catch(Exception e) {}
		// invalid product code
		try {
			pt = new ProductTypeClass(1, "null", "40063813339", 1.0, null);
			fail();
			}catch(InvalidProductCodeException e) {}
		catch(Exception e) {fail();}
		
		// invalid price
		try {
			pt = new ProductTypeClass(1, "null", "4006381333900", -0.0, null);
			fail();
			}catch(InvalidPricePerUnitException e) {}
		catch(Exception e) {fail();}
		// valid
		try {
			pt = new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");
			assertEquals("4006381333900", pt.getBarCode());
			assertEquals(new Integer(1), pt.getId());
			assertEquals("notes", pt.getNote());
			assertEquals("null", pt.getProductDescription());
			assertEquals(2.0, pt.getPricePerUnit(), 0.0001);
			// test on set note call
			pt.setNote("prova");
		}catch(Exception e) {fail();}
	}
	
	@Test
	public void testUpdateQuantity() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		ProductTypeClass pt = new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");
		// initial qty should be 0
		assertEquals(new Integer(0), pt.getQuantity());
		// try update with negative
		assertFalse(pt.updateQuantity(-1));
		assertEquals(new Integer(0), pt.getQuantity());
		assertTrue(pt.updateQuantity(2));
		assertTrue(pt.updateQuantity(0));
		assertEquals(new Integer(2), pt.getQuantity());
	}
	
	@Test
	public void testDescription() {
		ProductTypeClass pt=null;
		try {
			pt = new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");
		} catch (Exception e1) {}
		assertEquals("null", pt.getProductDescription());
		// invalid new description
		try {
			pt.setProductDescription("");
			fail();
		}catch(Exception e) {}
		assertEquals("null", pt.getProductDescription());
		try {
			pt.setProductDescription(null);
			fail();
		}catch(Exception e) {}
		assertEquals("null", pt.getProductDescription());
		// valid description
		try {
			pt.setProductDescription("prova");			
		}catch(Exception e) {fail();}
		assertEquals("prova", pt.getProductDescription());		
	}
	
	@Test
	public void testProductId() {
		ProductTypeClass pt=null;
		try {
			pt = new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");
		} catch (Exception e1) {}
		// neagtive 
		try {
			pt.setId(-1);
			fail();
		}catch(Exception e) {}
		// null
		try {
			pt.setId(null);
			fail();
		}catch(Exception e) {}
		
		// valid
		try {
			pt.setId(1231);
			assertEquals(new Integer(1231), pt.getId());
		}catch(Exception e) {fail();}
	}
	
	@Test
	public void testSetPosition() {
		ProductTypeClass pt=null;
		try {
			pt = new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");
		} catch (Exception e1) {}

		pt.setLocation("");
		assertEquals("", pt.getLocation());

		pt.setLocation((String)null);
		assertEquals("", pt.getLocation());
		//valid
		pt.setLocation("1_a_1");
		assertEquals("1_a_1", pt.getLocation());
		
		// with object position
		pt.setLocation((Position)null);
		assertEquals("", pt.getLocation());
		Position p = new Position("1_a_2");
		pt.setLocation(p);
		assertEquals("1_a_2", pt.getLocation());
	}
}