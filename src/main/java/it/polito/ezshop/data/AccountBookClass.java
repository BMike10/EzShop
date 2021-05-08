package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;

import java.util.Map;

public class AccountBookClass implements AccountBook{
    //Our Design

    private double balance;
    private Map<Integer,SaleTransaction> saleTransactionMap;
    private Map<Integer,Order> orderMap;
    private Map<Integer,ReturnTransaction> returnTransactionMap;
    private Map<Integer,BalanceOperation> balanceOperationMap;

    // Account Book Default Constructor
    public AccountBookClass(double balance) {
        this.balance = balance;
    }

    // Already existed Account Book
    public AccountBookClass(int balance, Map<Integer,SaleTransaction> SalOp, Map<Integer,Order> OrdOp, Map<Integer,ReturnTransaction> RetOp, Map<Integer,BalanceOperation> BalOp) {
        this.balance = balance;
        this.saleTransactionMap.putAll(SalOp);
        this.returnTransactionMap.putAll(RetOp);
        this.orderMap.putAll(OrdOp);
        this.balanceOperationMap.putAll(BalOp);
    }


    @Override
    public Integer addSaleTransaction(SaleTransaction saleTransaction) {
        //Sale Transaction is complete but without id
        Integer newId = newId();
        this.balanceOperationMap.put(newId, (BalanceOperation) saleTransaction);
        this.saleTransactionMap.put(newId, saleTransaction);
        return newId;
    }

    @Override
    public Integer addReturnTransaction(ReturnTransaction returnTransaction) {
        Integer newId = newId();
        this.balanceOperationMap.put(newId, (BalanceOperation) returnTransaction);
        this.returnTransactionMap.put(newId, returnTransaction);
        return newId;

    }

    @Override
    public Integer addOrder(Order order){
        Integer newId = newId();
        this.balanceOperationMap.put(newId, (BalanceOperation) order);
        this.orderMap.put(newId, order);
        return newId;
    }



    //In Design Transaction Objects are passed
    @Override
    public void removeSaleTransaction(Integer saleTransactionId) throws InvalidTransactionIdException {

        if(!this.saleTransactionMap.containsKey(saleTransactionId))
            throw new InvalidTransactionIdException();

        this.saleTransactionMap.remove(saleTransactionId);
        this.balanceOperationMap.remove(saleTransactionId);
    }

    @Override
    public void removeReturnTransaction(Integer returnTransactionId) throws InvalidTransactionIdException {
        if(!this.returnTransactionMap.containsKey(returnTransactionId))
            throw new InvalidTransactionIdException();

        this.returnTransactionMap.remove(returnTransactionId);
        this.balanceOperationMap.remove(returnTransactionId);
    }

    @Override
    public void removeOrder(Integer orderTransactionId) throws InvalidTransactionIdException {
        if(!this.orderMap.containsKey(orderTransactionId))
            throw new InvalidTransactionIdException();
        this.saleTransactionMap.remove(orderTransactionId);
        this.balanceOperationMap.remove(orderTransactionId);
    }

    @Override
    public SaleTransaction getSaleTransaction(int id) {
        return this.saleTransactionMap.get(id);
    }

    @Override
    public ReturnTransaction getReturnTransaction(int id) {
        return this.returnTransactionMap.get(id);
    }

    @Override
    public Order getOrder(int id) {
        return this.orderMap.get(id);
    }

    @Override
    public Double getBalance() {
        return this.balance;
    }

    @Override
    public void updateBalance(double amount) {
        this.balance = amount;
    }

    public Integer newId(){
        Integer newKey = null;
        //What if map is empty?
        for (Integer key : balanceOperationMap.keySet())
        {
            if (newKey == null || key.compareTo(newKey) > 0)
            {
                newKey = key;
            }
        }
        return newKey;
    }
}
