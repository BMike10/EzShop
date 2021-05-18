package it.polito.ezshop;

import it.polito.ezshop.data.Position;
import it.polito.ezshop.data.ProductTypeClass;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicketEntryTest {
    @Test
    public void testWhiteBox() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
        final ProductTypeClass pt=new ProductTypeClass(1, "null", "4006381333900", 2.0, "notes");

        // setQuantity
        assertThrows(RuntimeException.class, ()->{pt.setQuantity(null);});
        assertThrows(RuntimeException.class, ()->{pt.setQuantity(-1);});
        try {
            pt.setQuantity(2);
            assertEquals(new Integer(2), pt.getQuantity());
        }catch(Exception e) {
            fail();
        }
        // setBarCode
        assertThrows(RuntimeException.class, ()->{pt.setBarCode(null);});
        assertThrows(RuntimeException.class, ()->{pt.setBarCode("  dfs");});
        try {
            pt.setBarCode("400638133390");
            assertEquals("400638133390", pt.getBarCode());
        }catch(Exception e) {
            fail();
        }
        // setPricePerUnit
        assertThrows(RuntimeException.class, ()->{pt.setPricePerUnit(null);});
        assertThrows(RuntimeException.class, ()->{pt.setPricePerUnit(-0.0);});
        try {
            pt.setPricePerUnit(2.0);
            assertEquals(2.0, pt.getPricePerUnit(), 1e-6);
        }catch(Exception e) {
            fail();
        }

        // getPosition
        Position p = new Position("3-c-3");
        pt.setLocation(p);
        assertEquals(p, pt.getPosition());
    }
}
