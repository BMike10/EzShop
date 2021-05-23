package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;

public class ReturnTransactionClass extends BalanceOperationClass implements ReturnTransaction {

	// Map with the barcode and quantity of product in the sale transaction
	private final Map<ProductType, Integer> returnedProduct = new HashMap<>();
	private SaleTransaction saleTransaction;
	private ReturnStatus status;
	/*
	 * //no reference public ReturnTransactionClass(double amount, String type,
	 * Map<ProductType,Integer> returned, SaleTransaction saleT, ReturnStatus
	 * retstatus) { super(amount, type); this.returnedProduct.putAll(returned);
	 * this.saleTransaction = saleT; this.status = retstatus; try {
	 * super.setDescription("RETURN"); } catch (Exception e) { e.printStackTrace();
	 * } }
	 */

	public ReturnTransactionClass(int orderId, String description, double amount, LocalDate date, String type,
			Map<ProductType, Integer> returned, SaleTransaction saleT, ReturnStatus retstatus) {
		super(orderId, description, amount, date, type);
		if (amount < 0)
			throw new RuntimeException(new InvalidQuantityException());
		if (date == null)
			throw new RuntimeException(new Exception());
		if (type == null || type != "DEBIT")
			throw new RuntimeException(new Exception());
		if (orderId < 0)
			throw new RuntimeException(new InvalidTransactionIdException());
		if (description == null)
			throw new RuntimeException(new Exception());
		if (returned == null)
			throw new RuntimeException(new Exception());
		if (saleT == null)
			throw new RuntimeException(new Exception());
		if (retstatus == null)
			throw new RuntimeException(new Exception());
		this.returnedProduct.putAll(returned);
		this.saleTransaction = saleT;
		this.status = retstatus;
	}

	public ReturnTransactionClass(SaleTransaction saleT, ReturnStatus retstatus) {
		super(-1, "RETURN", 0.0, LocalDate.now(), "DEBIT");
		if (saleT == null)
			throw new RuntimeException(new Exception());
		this.saleTransaction = saleT;
		try {
			this.setDescription("RETURN");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.status = retstatus;
	}

	@Override
	public Integer getReturnId() {
		return super.getBalanceId();
	}

	@Override
	public void setReturnId(Integer balanceId) {
		if (balanceId == null || balanceId < 0)
			throw new RuntimeException(new Exception());
		super.setBalanceId(balanceId);
	}

	@Override
	public Map<ProductType, Integer> getReturnedProduct() {
		return returnedProduct;
	}

	@Override
	public void setReturnedProduct(Map<ProductType, Integer> returnedProduct) {
		if (returnedProduct == null)
			throw new RuntimeException(new Exception());
		this.returnedProduct.clear();
		this.returnedProduct.putAll(returnedProduct);
	}

	@Override
	public SaleTransaction getSaleTransaction() {
		return saleTransaction;
	}

	@Override
	public void setSaleTransaction(SaleTransaction saleTransaction) {
		if (saleTransaction == null)
			throw new RuntimeException(new Exception());
		this.saleTransaction = saleTransaction;
	}

	@Override
	public String getStatus() {
		return status.name();
	}

	@Override
	public void setStatus(String status) {
		if (status == null)
			throw new RuntimeException(new Exception());
		try {
			this.status = ReturnStatus.valueOf(status);
		} catch (Exception e) {
			throw new RuntimeException(new Exception());
		}
	}

	@Override
	public double getMoney() {
		return super.getMoney();
	}

	@Override
	public void setMoney(double money) {
		if (money < 0)
			throw new RuntimeException(new Exception());
		super.setMoney(money);
	}

	public int addReturnProduct(ProductType product, int quantity) {
		if (product == null)
			throw new RuntimeException(new Exception());
		if (quantity <= 0)
			throw new RuntimeException(new InvalidQuantityException());
		SaleTransactionClass st = (SaleTransactionClass) this.saleTransaction;
		int amount = st.getProductsEntries().get(product.getBarCode()).getAmount();
		if (amount < quantity)
			return -1;
		this.returnedProduct.put(product, quantity);
		setMoney(getMoney() + product.getPricePerUnit() * quantity
				* (1 - st.getProductsEntries().get(product.getBarCode()).getDiscountRate()));
		st.deleteProduct(product, quantity);
		return 1;
	}

}
