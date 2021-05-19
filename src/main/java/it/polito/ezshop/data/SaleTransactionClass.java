package it.polito.ezshop.data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;


public class SaleTransactionClass extends BalanceOperationClass implements SaleTransaction {

	public SaleTransactionClass(double price, String paymentType, Time time, SaleStatus status, LoyaltyCard loyaltyCard,
			Integer ticketNumber, Map<String, TicketEntryClass> ticketEntries, double discountRate) throws Exception {
		super(ticketNumber, "SALE", price, LocalDate.now(), "CREDIT");
		if(price<0) throw new RuntimeException(new Exception());									//OK?
		if(time==null) throw new RuntimeException(new Exception());
		if(status==null) throw new RuntimeException(new Exception());
		if(ticketNumber<0) throw new RuntimeException(new InvalidTransactionIdException());
		if(ticketEntries==null) throw new RuntimeException(new Exception());
		if(discountRate<0 || discountRate>1) throw new InvalidDiscountRateException();
		if ((!paymentType.equals("CASH") && !paymentType.equals("CREDIT_CARD")) || paymentType==null)
			throw new RuntimeException(new InvalidPaymentException());					//OK?
		if(loyaltyCard==null) 			throw new RuntimeException(new InvalidCustomerCardException());
		this.setPaymentType(paymentType);
		this.time = time;
		this.status = status;
		this.loyaltyCard = loyaltyCard;
		this.ticketEntries = ticketEntries;
		this.discountRate = discountRate;
	}

	public SaleTransactionClass(Time time, SaleStatus saleStatus) {
		this(-1, "SALE", 0.0, LocalDate.now(), "CREDIT", "", time, saleStatus, null, new HashMap<>(), 0.0);
	}
	/*// no reference
	public SaleTransactionClass(Time time, String paymentType, SaleStatus status, LoyaltyCard loyaltyCard,
			Integer ticketNumber, Map<String, TicketEntryClass> ticketEntries) throws Exception {
		this(0.0, paymentType, time, status, loyaltyCard, ticketNumber, ticketEntries, 0.0);
	}
	// no reference
	public SaleTransactionClass(Time time, String paymentType, LoyaltyCard loyaltyCard, Integer ticketNumber,
			Map<String, TicketEntryClass> ticketEntries) throws Exception {
		this(0.0, paymentType, time, SaleStatus.STARTED, loyaltyCard, ticketNumber, ticketEntries, 0.0);

	}
	// no reference
	public SaleTransactionClass(Time time, String paymentType, LoyaltyCard loyaltyCard, Integer ticketNumber) throws Exception {
		this(0.0, paymentType, time, SaleStatus.STARTED, loyaltyCard, ticketNumber, new HashMap<>(), 0.0);
	}*/
	// New constructor
	public SaleTransactionClass(int transactionId, String description, double money, LocalDate date, String type,
			String paymentType, Time time, SaleStatus status, LoyaltyCard loyaltyCard,
			Map<String, TicketEntryClass> ticketEntries, double discountRate) {
		super(transactionId, description, money, date, type);
		this.setPaymentType(paymentType);
		this.time = time;
		this.status = status;
		this.loyaltyCard = loyaltyCard;
		this.ticketEntries = ticketEntries;
		this.discountRate = discountRate;
	}

	private Time time;
	private SaleStatus status;
	private LoyaltyCard loyaltyCard;
	// (barcode, TicketEntryClass)
	private Map<String, TicketEntryClass> ticketEntries;
	private double discountRate;
	private String paymentType = "";

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		if(time==null) throw new RuntimeException(new Exception());
		this.time = time;
	}

	public void setStatus(SaleStatus status) {
		if(status==null) throw new RuntimeException(new Exception());
		this.status = status;
	}

	public void setLoyaltyCard(LoyaltyCard l) {								//non testare
		if(l==null) throw new RuntimeException(new InvalidCustomerCardException());
		this.loyaltyCard = l;
	}

	public LoyaltyCard getLoyaltyCard() {									//non testare
		return this.loyaltyCard;
	}

	public Map<String, TicketEntryClass> getProductsEntries() {				//non testare
		return this.ticketEntries;
	}

	@Override
	public Integer getTicketNumber() {
		return super.getBalanceId();
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		if(ticketNumber<0 || ticketNumber==null) throw new RuntimeException(new InvalidTransactionIdException());
		super.setBalanceId(ticketNumber);
	}

	@Override
	public List<TicketEntry> getEntries() {
		List<TicketEntry> res = new ArrayList<TicketEntry>();
		ticketEntries.forEach((s, te)->{
			try {
				res.add(new TicketEntryClass(te.getProductType(), te.getAmount(), te.getDiscountRate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return res;
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {						//non testare
		if(entries==null) throw new RuntimeException(new Exception());
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
		if(discountRate<0 || discountRate>1) throw new RuntimeException(new InvalidDiscountRateException());
		this.discountRate = discountRate;
	}
	
	//SHOULD RETURN BOOLEAN SO I CAN TEST IT!
	public boolean addProduct(ProductType product, int quantity) {					//non testare
		if(product==null) return false;									//CHANGED --- this should also accept quantity <0 for return transaction?
		if(quantity==0) return false;
		if (ticketEntries.containsKey(product.getBarCode())) {
			TicketEntryClass t = ticketEntries.get(product.getBarCode());
			t.setAmount(t.getAmount() + quantity);
			ticketEntries.remove(product.getBarCode());
			ticketEntries.put(product.getBarCode(), t);
		} else {
			TicketEntryClass t;
			try {
				t = new TicketEntryClass(product, quantity);
				ticketEntries.put(product.getBarCode(), t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		double a = this.getPrice();
		a += product.getPricePerUnit() * quantity * (1 - this.discountRate);
		this.setPrice(a);
		return true;
	}

	public void deleteProduct(ProductType product, int quantity) {                  //non testare
		if(quantity==0) throw new RuntimeException(new InvalidQuantityException());
		if (ticketEntries.containsKey(product.getBarCode())) {
			TicketEntryClass t = ticketEntries.get(product.getBarCode());
			t.setAmount(t.getAmount() - quantity);
			if (t.getAmount() > 0) {
				ticketEntries.remove(t.getBarCode());
				ticketEntries.put(product.getBarCode(), t);
			} else {
				ticketEntries.remove(t.getBarCode());
			}
		}
	}

	public SaleStatus getStatus() {
		return this.status;
	}

	public void checkout() {															//non testare
		double a = 0.0;
		if(ticketEntries.size()==0) {
			this.setPrice(a);
			this.status=SaleStatus.CLOSED;
			return;
		}
		for (TicketEntryClass te : ticketEntries.values()) {
			a += (te.getPricePerUnit() * te.getAmount()) * (1 - te.getDiscountRate());
		}
		a = a * (1 - this.getDiscountRate());
		this.setPrice(a);
		this.status = SaleStatus.CLOSED;
	}

	public void addProductDiscount(ProductType product, double discount) {				
		if(discount<0 || discount>1) throw new RuntimeException();
		if (ticketEntries.containsKey(product.getBarCode())) {
			ticketEntries.get(product.getBarCode()).setDiscountRate(discount);
		}
	}

	@Override
	public double getPrice() {
		return super.getMoney();
	}

	@Override
	public void setPrice(double price) {										
		if(price<0) throw new RuntimeException(new Exception());
		super.setMoney(price);
	}
	/*// no references
	public Map<String, TicketEntryClass> getTicketEntries() {					
		return this.ticketEntries;
	}*/

	public String getPaymentType() {											
		return paymentType;
	}

	public void setPaymentType(String paymentType) {							
		if(paymentType!="CASH" && paymentType!="CREDIT_CARD") throw new RuntimeException(new InvalidPaymentException());
		this.paymentType = paymentType;
	}
}