package it.polito.ezshop.data;

import java.sql.SQLException;
import java.sql.Statement;

import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public enum RoleEnum {
	ADMIN, CASHIER, SHOPMANAGER
}

