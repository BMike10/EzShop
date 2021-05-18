package it.polito.ezshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import it.polito.ezshop.data.CustomerClass;
import it.polito.ezshop.data.LoyaltyCard;
import it.polito.ezshop.data.LoyaltyCardClass;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;

public class LoyaltyCardClassTest {
	@Test
	public void testLoyaltyCardClassConstructor() {
		//invalid cardCode
		assertThrows(Exception.class, ()->{
			final LoyaltyCardClass card = new LoyaltyCardClass("A4FBH67NDTSYNH", 0);});		
				try {
				final LoyaltyCardClass card = new LoyaltyCardClass("A4FBH67NDT", 0);					
				assertEquals("A4FBH67NDT", card.getCardCode());		
				}catch(Exception e) {				
					fail();
					}
	
	}
	  @Test
	    public void testSetPoints(){
	       LoyaltyCard card = new LoyaltyCardClass("A4FBH67NDT", 0);
	        assertTrue(card.setPoints(10));
	        assertFalse(card.setPoints(-10));
	    }
	  
	  @Test
		public void testUpdatePoints() throws InvalidCustomerCardException {
			LoyaltyCardClass card = new LoyaltyCardClass("A4FBH67NDT", 0);		
			// initial points should be 0
			assertEquals(new Integer(0), card.getPoints());
			// try update with negative
			assertFalse(card.updatePoints(-1));
			assertEquals(new Integer(0), card.getPoints());
			assertTrue(card.updatePoints(10));
			assertTrue(card.updatePoints(10));
			assertEquals(new Integer(20), card.getPoints());
		}
		
	  @Test
	  public void testCreateCardCode(){
		  int i=11;
			assertEquals("", LoyaltyCardClass.createCardCode(i));
			int i2=9;
			assertEquals("", LoyaltyCardClass.createCardCode(i2));
	  }
	  
	  @Test
		public void testWhiteBox() {
			final LoyaltyCardClass card = new LoyaltyCardClass("A4FBH67NDT", 0);
			assertThrows(RuntimeException.class, () -> {card.setCardCode(null);});
			assertThrows(RuntimeException.class, () -> {card.setCardCode("abc");});
			assertThrows(RuntimeException.class, () -> {card.setCardCode("vgrbvrebretrwbvgtrw");});			
	  
	  }

	
	

}
