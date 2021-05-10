package it.polito.ezshop.data;

public class LoyaltyCardClass implements LoyaltyCard {
	private int points;
	private String cardCode;
	
	public LoyaltyCardClass(String cardCode, int points)
	{
		super();
		this.points=0;
		this.cardCode=cardCode;
		
	}
	

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	
	public boolean updatePoints(int toBeAdded) {
		if(points + toBeAdded < 0)
			return false;
		points += toBeAdded;
		return true;
	}

}

