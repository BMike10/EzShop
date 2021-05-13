package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

import it.polito.ezshop.data.CustomerClass;
import it.polito.ezshop.data.RoleEnum;
import it.polito.ezshop.data.UserClass;
import it.polito.ezshop.data.CustomerClass;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class CustomerClassTest {
	



/* 	 if(customerName==null ||customerName.isEmpty()) throw new InvalidCustomerNameException();
         if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException(
         if(newCustomerName==null ||newCustomerName.isEmpty()) throw new InvalidCustomerNameException();
    	if(newCustomerCard==null ||newCustomerCard.isEmpty()||!CustomerClass.checkCardCode(newCustomerCard)) throw new InvalidCustomerCardException();
        if(currentUser==null || currentUser.getRole().isEmpty()) throw new UnauthorizedException(); */
		 
		@Test
		public void testCustomerClassConstructor() {
			//invalid id
			assertThrows(InvalidCustomerIdException.class, ()->{
				CustomerClass c = new CustomerClass(0,"customerName","",0);});
			// invalid customerName
			assertThrows(InvalidCustomerNameException.class, ()->{
				CustomerClass c = new CustomerClass(1,null,"",0);});		
			// valid
					try {
						CustomerClass c = new CustomerClass(1, "customerName","",0);					
					}catch(Exception e) {fail();}
		
		}
		
		@Test
		public void testSetCustomerId() {
			final CustomerClass c = new CustomerClass(1,"customerName","",0);					
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
			final CustomerClass c = new CustomerClass(1,"customerName","",0);					
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
		
		
}
