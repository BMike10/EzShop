package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import it.polito.ezshop.data.CustomerClass;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;

public class CustomerClassTest {
		
	@Test
		public void testCustomerClassConstructor() {
			//invalid id
			assertThrows(Exception.class, ()->{
				final CustomerClass c = new CustomerClass(0,"customerName","abcde12345",0);});
			// invalid customerName
			assertThrows(Exception.class, ()->{
				final CustomerClass c = new CustomerClass(1,"","abcde12345",0);});		
			// valid
					try {
					final CustomerClass c = new CustomerClass(1, "customerName","abcde12345",0);					
					assertEquals(new Integer(1), c.getId());
					assertEquals("customerName", c.getCustomerName());
					assertEquals("abcde12345", c.getCustomerCard());
					assertEquals(new Integer(0), c.getPoints());
					}catch(Exception e) {				
						fail();
						}
		
		}
		
		@Test
		public void testSetCustomerId() {
			final CustomerClass c = new CustomerClass(1,"customerName","abcde12345",0);					
			assertEquals(new Integer(1), c.getId());
					// null
					assertThrows(Exception.class, ()->{c.setId(null);});
					assertEquals(new Integer(1), c.getId());
					// invalid
					assertThrows(Exception.class, ()->{c.setId(-1);});
					assertEquals(new Integer(1), c.getId());
					// valid
					try {
						c.setId(2);
					}catch(Exception e) {
						fail();
					}

			}
		@Test
		public void testSetCustomerName() {
			final CustomerClass c = new CustomerClass(1,"customerName","abcde12345",0);					
			assertEquals("customerName", c.getCustomerName());
					// null
					assertThrows(Exception.class, ()->{c.setCustomerName(null);});
					assertEquals("customerName",c.getCustomerName());
					// empty
					assertThrows(Exception.class, ()->{c.setCustomerName("");});
					assertEquals("customerName", c.getCustomerName());
					// valid
					try {
						c.setCustomerName("customerName");
					}catch(Exception e) {
						fail();
					}
		}
		@Test
		public void testSetCustomerCard() {
			//valid 10 digits
			String code10 = "abcde12345";
			assertTrue(CustomerClass.checkCardCode(code10));
			//invalid 11 
			String code11 = "abcde123456";
			assertFalse(CustomerClass.checkCardCode(code11));
			//invalid 9 
			String code9 = "abcde1234";
			assertFalse(CustomerClass.checkCardCode(code9));
		}
		
		@Test
		public void testCustomerId() {
			CustomerClass c =null;
			try {
				c = new CustomerClass(1, "alice","abcde12345", 0);
			} catch (Exception e1) {fail();}
			// negative 
			try {
				c.setId(-1);
				fail();
			}catch(Exception e) {}
			// null
			try {
				c.setId(null);
				fail();
			}catch(Exception e) {}
			
			// valid
			try {
				c.setId(1231);
				assertEquals(new Integer(1231), c.getId());
			}catch(Exception e) {fail();}
		}
		
		//WB testing
		@Test 
		public void testWhiteBox() throws InvalidCustomerCardException, InvalidCustomerNameException,InvalidCustomerIdException
		{	final CustomerClass c = new CustomerClass(1,"customerName","abcde12345",0);					
			//setId
		assertThrows(RuntimeException.class, () -> {c.setId(null);});
		assertThrows(RuntimeException.class, () -> {c.setId(0);});
		try {
			c.setId(2);
			assertEquals(new Integer(2), c.getId());
		} catch(Exception e) {
			fail();
		}
			//setName
			assertThrows(RuntimeException.class, () -> {c.setCustomerName(null);});
			assertThrows(RuntimeException.class, () -> {c.setCustomerName("");});
			try {
				c.setCustomerName("username");
				assertEquals("username", c.getCustomerName());
			} catch(Exception e) {
				fail();
			}
			
			//setCard
			assertThrows(RuntimeException.class, () -> {c.setCustomerCard(null);});
			try {
				c.setCustomerCard("abcde12345");
				assertEquals("abcde12345", c.getCustomerCard());
			} catch(Exception e) {
				fail();
			}
			

		}
		
		
}
