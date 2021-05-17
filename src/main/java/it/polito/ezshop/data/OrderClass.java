package it.polito.ezshop.data;

import java.time.LocalDate;

import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidQuantityException;

public final class OrderClass extends BalanceOperationClass implements Order {
	//private int orderId;
	private String productCode;
	private double pricePerUnit;
	private int quantity;
	private OrderStatus status;
	
	
	public OrderClass(String productCode, double pricePerUnit, int quantity, OrderStatus status) {
		this(-1, "ORDER", pricePerUnit * quantity, LocalDate.now(), (String)null, productCode, pricePerUnit, quantity, status);
	}
	public OrderClass(String productCode, double pricePerUnit, int quantity) {
		this(-1, "ORDER", pricePerUnit * quantity, LocalDate.now(), (String)null, productCode, pricePerUnit, quantity, OrderStatus.ISSUED);
	}
	public OrderClass(int id, String desc, double amount, LocalDate date, String supplier, String productCode, double pricePerUnit, int quantity, OrderStatus status) {
		super(id, desc, amount, date, "DEBIT");
		if(productCode == null || !ProductTypeClass.validateBarCode(productCode))
			throw new RuntimeException(new InvalidProductCodeException());
		if(pricePerUnit <= 0)
			throw new RuntimeException(new InvalidPricePerUnitException());
		if(quantity <= 0)
			throw new RuntimeException(new InvalidQuantityException());
		this.productCode = productCode;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
		this.status = status;
	}
	@Override
	public Integer getBalanceId() {
		return super.getBalanceId();
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
		if(productCode == null || !ProductTypeClass.validateBarCode(productCode))
			throw new RuntimeException(new InvalidProductCodeException());
		this.productCode = productCode;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		if(pricePerUnit <= 0)
			throw new RuntimeException(new InvalidPricePerUnitException());
		this.pricePerUnit = pricePerUnit;
		setMoney(quantity * pricePerUnit);
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		if(quantity <= 0)
			throw new RuntimeException(new InvalidQuantityException());
		this.quantity = quantity;
		setMoney(quantity * pricePerUnit);
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
		return getBalanceId();
	}

	@Override
	public void setOrderId(Integer orderId) {
		if(orderId == null || orderId <= 0)
			throw new RuntimeException(new InvalidOrderIdException());
		super.setBalanceId(orderId);
	}

	@Override
	public double getMoney() {
		return super.getMoney();
	}
}
