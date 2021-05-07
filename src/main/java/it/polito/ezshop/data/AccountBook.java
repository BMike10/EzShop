package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;

public interface AccountBook {

    Integer addSaleTransaction(SaleTransaction saleTransaction) throws InvalidTransactionIdException;
    Integer addReturnTransaction(ReturnTransaction returnTransaction) throws InvalidTransactionIdException;
    Integer addOrder(Order order) throws InvalidTransactionIdException;
    Integer addTransaction(BalanceOperation bo) throws InvalidTransactionIdException;



    //In Design Transaction Objects are passed
    void removeSaleTransaction(Integer saleTransactionId) throws InvalidTransactionIdException;
    void removeReturnTransaction(Integer returnTransactionId) throws InvalidTransactionIdException;
    void removeOrder(Integer orderTransactionId) throws InvalidTransactionIdException;


    SaleTransaction getSaleTransaction(int id);
    ReturnTransaction getReturnTransaction(int id);
    Order getOrder(int id);
    Double getBalance();
    void updateBalance(double amount);



}
