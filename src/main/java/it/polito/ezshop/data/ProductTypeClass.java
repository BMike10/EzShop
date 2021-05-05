package it.polito.ezshop.data;

public final class ProductTypeClass implements ProductType {
	private String description;
	private String notes;
	private int quantity;
	private Position location;
	private String barcode;
	private int id;
	private double unitPrice;
	
	
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
		return location.toString();
	}

	@Override
	public void setLocation(String location) {
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

}
