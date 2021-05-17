package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;

public class LoyaltyCardClass implements LoyaltyCard {
	private int points;
	private String cardCode;
	
	public LoyaltyCardClass(String cardCode, int points)
	{
		this.points=points;
		this.cardCode=cardCode;
		
	}
		
public String createCardCode(int i) 
	    { 
	        String theAlphaNumericS;
	        StringBuilder builder;
	        
	        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	                                    + "0123456789"; 

	        //create the StringBuffer
	        builder = new StringBuilder(i); 

	        for (int m = 0; m < i; m++) { 

	            // generate numeric
	            int myindex 
	                = (int)(theAlphaNumericS.length() 
	                        * Math.random()); 

	            // add the characters
	            builder.append(theAlphaNumericS 
	                        .charAt(myindex)); 
	        } 

	        return builder.toString(); 
	    } 

	

	public Integer getPoints() {
		return points;
	}

	public boolean setPoints(Integer points) {
		if(points<0) return false;
		this.points = points;
		return true;
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

