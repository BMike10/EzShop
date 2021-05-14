package it.polito.ezshop.data;

public class TicketEntryClass implements TicketEntry {

	private ProductType productType;
	private int amount;
	private double discountRate;

	public TicketEntryClass(ProductType p, int amount, double discRate) {
		this.productType=p;
		this.discountRate=discRate;
		this.amount=amount;
	}
	public TicketEntryClass(ProductType p, int amount) {
		this.productType=p;
		this.discountRate=0.0;
		this.amount=amount;
	}
	public ProductType getProductType() {
		return this.productType;
	}
	@Override
	public String getBarCode() {
		return this.productType.getBarCode();
	}

	@Override
	public void setBarCode(String barCode) {
		this.productType.setBarCode(barCode);
	}

	@Override
	public String getProductDescription() {
		return this.productType.getProductDescription();
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.productType.setProductDescription(productDescription);
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount=amount;
	}

	@Override
	public double getPricePerUnit() {
		return this.productType.getPricePerUnit();
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.productType.setPricePerUnit(pricePerUnit);
	}

	@Override
	public double getDiscountRate() {
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate=discountRate;
	}

}
