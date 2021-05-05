package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidLocationException;

public class Position {
	private int aisleId;
	private String rackId;
	private int levelId;
	
	public Position(String position) throws InvalidLocationException {
		if(position == null || position.length() <= 0) {
			aisleId = -1;
			rackId = "";
			levelId = -1;
			return;
		}
		String[] fields = position.split("_");
		if(fields.length != 3) {
			throw new InvalidLocationException("Invalid position string: " + position);
		}
		try {
		aisleId = Integer.parseInt(fields[0]);
		rackId = fields[1];
		aisleId = Integer.parseInt(fields[2]);
		}catch(Exception e) {
			throw new InvalidLocationException("Invalid position string: " + position);
		}
	}
	
	public int getAisleId() {
		return aisleId;
	}
	public String getRackId() {
		return rackId;
	}
	public int getLevelId() {
		return levelId;
	}	
	
}
