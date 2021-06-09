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
	
	public static String calculateRFID(String input) {				
		String result = "";				
		String numberStr = "";
		
		int i = input.length() - 1;
		for(; i > 0; i--) {
			
			char c = input.charAt(i);
			
			if(!Character.isDigit(c))
				break;
			
			numberStr = c + numberStr;
		}
		
		int number = Integer.parseInt(numberStr);
		number++;
		
		result += input.substring(0, i + 1);
		result += number < 10 ? "0" : "";
		result += number;
		
		return result;
}
}
