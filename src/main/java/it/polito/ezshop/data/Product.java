package it.polito.ezshop.data;

public class Product {
	private String RFID;
	private ProductTypeClass productType;
	
	
	public Product(String rFID, ProductTypeClass productType) {
		super();
		if(rFID == null || !rFID.matches("\\d{10}"))
			throw new RuntimeException();
		if(productType == null)
			throw new RuntimeException();
		RFID = rFID;
		this.productType = productType;
	}
	public String getRFID() {
		return RFID;
	}
	public void setRFID(String rFID) {
		if(rFID == null || !rFID.matches("\\d{10}"))
			throw new RuntimeException();
		RFID = rFID;
	}
	public ProductTypeClass getProductType() {
		return productType;
	}
	public void setProductType(ProductTypeClass productType) {
		if(productType == null)
			throw new RuntimeException();
		this.productType = productType;
	}
	
	
	
}
