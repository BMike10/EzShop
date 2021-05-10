package it.polito.ezshop.data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

public class SaleTransactionClass extends BalanceOperationClass implements SaleTransaction {

	public SaleTransactionClass(Time time, String paymentType, SaleStatus status, LoyaltyCard loyaltyCard,
			Integer ticketNumber, Map<String, TicketEntryClass> ticketEntries, double discountRate, double price) {
		super(price, paymentType);
		this.time = time;
		this.paymentType = paymentType;
		this.status = status;
		this.loyaltyCard = loyaltyCard;
		this.ticketNumber = ticketNumber;
		this.ticketEntries = ticketEntries;
		this.discountRate = discountRate;
		this.price = price;

	}

	public SaleTransactionClass(Time time, String paymentType, SaleStatus status, LoyaltyCard loyaltyCard,
			Integer ticketNumber, Map<String, TicketEntryClass> ticketEntries) {
		super(0.0, paymentType);
		this.time = time;
		this.paymentType = paymentType;
		this.status = status;
		this.loyaltyCard = loyaltyCard;
		this.ticketNumber = ticketNumber;
		this.ticketEntries = ticketEntries;
		this.discountRate = 0.0;
		this.price = 0.0;

	}
	
	public SaleTransactionClass(Time time, String paymentType, LoyaltyCard loyaltyCard,
			Integer ticketNumber, Map<String, TicketEntryClass> ticketEntries) {
		super(0.0, paymentType);
		this.time = time;
		this.paymentType = paymentType;
		this.status = SaleStatus.STARTED;
		this.loyaltyCard = loyaltyCard;
		this.ticketNumber = ticketNumber;
		this.ticketEntries = ticketEntries;
		this.discountRate = 0.0;
		this.price = 0.0;

	}
	
	public SaleTransactionClass(Time time, String paymentType, LoyaltyCard loyaltyCard,
			Integer ticketNumber) {
		super(0.0, paymentType);
		this.time = time;
		this.paymentType = paymentType;
		this.status = SaleStatus.STARTED;
		this.loyaltyCard = loyaltyCard;
		this.ticketNumber = ticketNumber;
		this.ticketEntries = new HashMap<>();
		this.discountRate = 0.0;
		this.price = 0.0;

	}
	
	private Time time;
	private String paymentType;
	private SaleStatus status;
	
	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	private LoyaltyCard loyaltyCard;
	private Integer ticketNumber;
	private Map<String, TicketEntryClass> ticketEntries;
	private double discountRate = 0.0;
	private double price = 0.0;
	
	public void setLoyaltyCard(LoyaltyCard l) {
		this.loyaltyCard=l;
	}
	public LoyaltyCard getLoyaltyCard() {
		return this.loyaltyCard;
	}
	public Map<String, TicketEntryClass> getProductsEntries() {
		return this.ticketEntries;
	}
	@Override
	public Integer getTicketNumber() {
		return this.ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return new ArrayList<TicketEntry>(this.ticketEntries.values());
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		this.ticketEntries = new HashMap<>();
		for (int i = 0; i < entries.size(); i++) {
			this.ticketEntries.put(entries.get(i).getBarCode(), (TicketEntryClass) entries.get(i));
		}

	}
	
	@Override
	public double getDiscountRate() {
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}

	public void addProduct(ProductType product, int quantity) {
		if (ticketEntries.containsKey(product.getBarCode())) {
			TicketEntryClass t = ticketEntries.get(product.getBarCode());
			t.setAmount(t.getAmount() + quantity);
			ticketEntries.remove(product.getBarCode());
			ticketEntries.put(product.getBarCode(), t);
		} else {
			TicketEntryClass t = new TicketEntryClass(product.getBarCode(), product.getProductDescription(), quantity,
					product.getPricePerUnit(), this.getTicketNumber()); //////////////////////////////////////////////// ticketNumber?
			ticketEntries.put(product.getBarCode(), t);
		}
		this.price+=product.getPricePerUnit()*quantity*(1-this.discountRate);
	}

	public void deleteProduct(ProductType product, int quantity) {
		if (ticketEntries.containsKey(product.getBarCode())) {
			TicketEntryClass t = ticketEntries.get(product.getBarCode());
			t.setAmount(t.getAmount() - quantity);
			if (t.getAmount() != 0) {
				ticketEntries.remove(t.getBarCode());
				ticketEntries.put(product.getBarCode(), t);
			}
			else {
				ticketEntries.remove(t.getBarCode());
			}
		}
	}
	public SaleStatus getStatus() {
		return this.status;
	}
	//QUANDO DOVRO' SETTARE A CLOSED E QUANDO A PAYED?
	public void checkout() {
		this.price=0.0;
		ticketEntries.values().forEach(e->{
			this.price+=(e.getPricePerUnit()*e.getAmount())*(1-e.getDiscountRate());
		});
		this.price=this.price*(1-this.getDiscountRate());
		this.setMoney(this.price);
		this.status=SaleStatus.CLOSED;
	}

	public void addProductDiscount(ProductType product, double discount) {
		if (ticketEntries.containsKey(product.getBarCode())) {
			ticketEntries.get(product.getBarCode()).setDiscountRate(discount);
		}
	}
	
	
}
