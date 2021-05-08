package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;

public interface AccountBook {

    Integer addSaleTransaction(SaleTransaction saleTransaction);
    Integer addReturnTransaction(ReturnTransaction returnTransaction);
    Integer addOrder(Order order);


    //In Design Transaction Objects are passed
    void removeSaleTransaction(Integer saleTransactionId) throws InvalidTransactionIdException;
    void removeReturnTransaction(Integer returnTransactionId) throws InvalidTransactionIdException;
    void removeOrder(Integer orderTransactionId) throws InvalidTransactionIdException;


    SaleTransaction getSaleTransaction(int id);
    ReturnTransaction getReturnTransaction(int id);
    Order getOrder(int id);
    Double getBalance();
    void updateBalance(double amount);
    Integer newId();


}
