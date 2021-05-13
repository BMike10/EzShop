package it.polito.ezshop.data;

public class CustomerClass implements Customer {

	private String customerName;
	private String customerCard;
	private Integer id;
	private Integer points;
	

	public CustomerClass(int id, String customerName, String customerCard, Integer points) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.customerCard = customerCard;
		this.points = points;
	}


	public static boolean checkCardCode(String newCustomerCard) {
		if(newCustomerCard.length() < 10) return false;
		return true;
	}


	@Override
	public String getCustomerName() {
		
		return customerName;
	}

	@Override
	public void setCustomerName(String customerName) {
		this.customerName=customerName;
		
	}

	@Override
	public String getCustomerCard() {
		return customerCard;
	}

	@Override
	public void setCustomerCard(String customerCard) {
		this.customerCard=customerCard;
		
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;		
	}

	@Override
	public Integer getPoints() {
		return points;
	}

	@Override
	public void setPoints(Integer points) {
		this.points=points;
		
	}

}
