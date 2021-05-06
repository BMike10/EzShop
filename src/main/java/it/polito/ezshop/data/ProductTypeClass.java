package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

public final class ProductTypeClass implements ProductType {
	private String description;
	private String notes;
	private int quantity;
	private Position location;
	private String barcode;
	private int id;
	private double unitPrice;
	
	
	public ProductTypeClass(int id, String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException{
		// checks
		if(description == null || description.length() <= 0)
        	throw new InvalidProductDescriptionException();
        if(productCode == null || !validateBarCode(productCode.trim()))
        	throw new InvalidProductCodeException();
        if(pricePerUnit <= 0.0)
        	throw new InvalidPricePerUnitException();
		this.id = id;
		this.description = description;
		this.barcode = productCode;
		this.unitPrice = pricePerUnit;
		this.notes = note;
		this.quantity = 0;
	}
	
    public static boolean validateBarCode(String barcode) {
    	if(barcode == null)
    		return false;
    	int len = barcode.length();
    	if(len < 12 || len > 14)
    		return false;
    	// compute the check digit
    	int digit = 0;
    	for(int i=0, j=0; i<14;i++) {
    		// if length is less than 14 skip some iterations
    		if(len < 14 - i)
    			continue;
    		try {
    			// add digit * (3 if even index, 1 if odd index) to result
    			digit += Integer.parseInt(""+barcode.charAt(j)) * (i%2==0 ? 3 : 1);
    		}catch(Exception e) {
    			return false;
    		}
    		j++;
    	}
    	// compute the rounded division to get the final check digit
    	digit = (digit + 5) / 10;
    	int lastDigit = Integer.parseInt(""+barcode.charAt(len - 1));
    	return digit == lastDigit;
    }
    
	public String getProductDescription() {
		return description;
	}

	public void setProductDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return notes;
	}

	public void setNote(String notes) {
		this.notes = notes;
	}

	public String getBarCode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setLocation(Position location) {
		this.location = location;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {
		if(location == null)
			return null;
		return location.toString();
	}

	@Override
	public void setLocation(String location){
		if(location == null) {
			this.location = null;
			return;
		}
		this.location = new Position(location);
	}


	@Override
	public void setBarCode(String barCode) {
		this.barcode = barCode;
	}

	@Override
	public Double getPricePerUnit() {
		return unitPrice;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		unitPrice = pricePerUnit;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public boolean updateQuantity(int toBeAdded) {
		if(quantity + toBeAdded < 0)
			return false;
		quantity += toBeAdded;
		return true;
	}
	public void setPosition(Position p) {
		this.location = p;
	}
	public Position getPosition() {
		return location;
	}
}
