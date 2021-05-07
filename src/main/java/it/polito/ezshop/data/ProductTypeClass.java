package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;

public final class ProductTypeClass implements ProductType {
	private String description = null;
	private String notes = null;
	private int quantity = 0;
	private Position location = null;
	private String barcode = null;
	private int id = 0;
	private double unitPrice = 0.0;
	
	
	public ProductTypeClass(int id, String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException{
		// checks
		if(description == null || description.length() <= 0)
        	throw new InvalidProductDescriptionException();
        if(productCode == null || !validateBarCode(productCode.trim()))
        	throw new InvalidProductCodeException();
        if(pricePerUnit <= 0.0)
        	throw new InvalidPricePerUnitException();
        if(id <= 0)
        	throw new RuntimeException(new InvalidProductIdException());
		this.id = id;
		this.description = description;
		this.barcode = productCode;
		this.unitPrice = pricePerUnit;
		this.notes = note;
		this.quantity = 0;
		this.location = null;
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
		if(description==null || description.length() <= 0)
			throw new RuntimeException(new InvalidProductDescriptionException());
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

	public void setLocation(Position location) {
		this.location = location;
	}

	@Override
	public Integer getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		if(quantity == null || quantity < 0)
			throw new RuntimeException(new InvalidQuantityException());
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
		if(barCode==null || !validateBarCode(barCode))
			throw new RuntimeException(new InvalidProductCodeException());
		this.barcode = barCode;
	}

	@Override
	public Double getPricePerUnit() {
		return unitPrice;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		if(pricePerUnit==null || pricePerUnit <= 0.0)
			throw new RuntimeException(new InvalidPricePerUnitException());
		unitPrice = pricePerUnit;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		if(id == null || id <= 0)
			throw new RuntimeException(new InvalidProductIdException());
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
