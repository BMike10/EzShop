package it.polito.ezshop.data;

public class TicketEntryClass implements TicketEntry {
	
	private String barCode;
	private String productDescription;
	private int amount;
	private double pricePerUnit;
	private double discountRate;
	private Integer ticketNumber;
	
	public TicketEntryClass(String barCode, String productDescription, int amount, Double pricePerUnit, int i) {
		// TODO Auto-generated constructor stub
		this.barCode=barCode;
		this.productDescription=productDescription;
		this.amount=amount;
		this.pricePerUnit=pricePerUnit;
		this.ticketNumber=i;
		this.discountRate=0.0;
	}
	public TicketEntryClass(String barCode, String productDescription, int amount, Double pricePerUnit, int i, double disc) {
		// TODO Auto-generated constructor stub
		this.barCode=barCode;
		this.productDescription=productDescription;
		this.amount=amount;
		this.pricePerUnit=pricePerUnit;
		this.ticketNumber=i;
		this.discountRate=disc;
	}

	@Override
	public String getBarCode() {
		return this.barCode;
	}

	public void setTicketNumber(Integer tickN) {
		this.ticketNumber=tickN;
	}
	
	public Integer getTicketNumber() {
		return this.ticketNumber;
	}
	
	@Override
	public void setBarCode(String barCode) {
		this.barCode=barCode;
	}

	@Override
	public String getProductDescription() {
		
		return this.productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.productDescription=productDescription;
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
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit=pricePerUnit;
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
