package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.data.Product;
import it.polito.ezshop.data.ProductTypeClass;
import it.polito.ezshop.data.SaleStatus;
import it.polito.ezshop.data.SaleTransactionClass;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import org.junit.Test;

import java.sql.Time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProductTest {
    @Test
    public void testSetProductType() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        //ProductType is null
        ProductTypeClass pT = new ProductTypeClass(1,"Banana","400638133390",1.0,null);
        Product p = new Product("111111111111",pT);
        assertThrows(Exception.class, () -> {p.setProductType(null);});

        //All right
        p.setProductType(pT);
        assertEquals(pT,p.getProductType());
    }

    @Test
    public void testSetRFID() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        //RFID is null
        ProductTypeClass pT = new ProductTypeClass(1,"Banana","400638133390",1.0,null);
        Product p = new Product("111111111111",pT);
        assertThrows(Exception.class, () -> {p.setRFID(null);});

        //RFID is not valid
        assertThrows(Exception.class, () -> {p.setRFID("111111111111111111");});

        //All right
        p.setRFID("111111111111");
        assertEquals("111111111111",p.getRFID());
    }

    @Test
    public void testCalculateRFID() throws InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        //Input is an invalid string
        ProductTypeClass pT = new ProductTypeClass(1,"Banana","400638133390",1.0,null);
        Product p = new Product("111111111111",pT);
        assertThrows(InvalidRFIDException.class, () -> {Product.calculateRFID("cia32444",1);});

        //step is negative
        assertThrows(InvalidRFIDException.class, () -> {Product.calculateRFID("2222",-1);});


    }
}
