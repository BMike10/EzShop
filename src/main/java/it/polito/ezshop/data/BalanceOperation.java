package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;

import java.time.LocalDate;

public interface BalanceOperation {

    //Can be changed interface? -> Order and SaleTransaction require Integer(Not Int)
    Integer getBalanceId();

    void setBalanceId(Integer balanceId);

    LocalDate getDate();

    void setDate(LocalDate date);

    double getMoney();

    void setMoney(double money);

    String getType();

    void setType(String type);
}

