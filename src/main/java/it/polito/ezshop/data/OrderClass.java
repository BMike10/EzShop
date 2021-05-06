package it.polito.ezshop.data;

public final class OrderClass implements Order {
	private int orderId;
	private String productCode;
	private ProductType product;
	private double pricePerUnit;
	private int quantity;
	private OrderStatus status;
	
	
	public OrderClass(int orderId, String productCode, double pricePerUnit, int quantity, OrderStatus status) {
		super();
		this.orderId = orderId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
	}
	public OrderClass(int orderId, String productCode, double pricePerUnit, int quantity) {
		super();
		this.orderId = orderId;
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = OrderStatus.ISSUED;
	}
	@Override
	public Integer getBalanceId() {
		return getOrderId();
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		setOrderId(balanceId);

	}

	@Override
	public String getProductCode() {
		return productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getStatus() {
		return status.name();
	}

	@Override
	public void setStatus(String status) {
		this.status = OrderStatus.valueOf(status);
	}
	public OrderStatus getOrderStatus() {
		return status;
	}
	@Override
	public Integer getOrderId() {
		return orderId;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
