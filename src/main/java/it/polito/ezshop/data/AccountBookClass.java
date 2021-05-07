package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;

import java.util.Map;

public class AccountBookClass implements AccountBook{
    //Our Design

    private int balanceOperationId;
    private double balance;
    private Map<Integer,SaleTransaction> saleTransactionMap;
    private Map<Integer,Order> orderMap;
    private Map<Integer,ReturnTransaction> returnTransactionMap;
    private Map<Integer,BalanceOperation> balanceOperationMap;

    // Account Book Default Constructor
    public AccountBookClass(double balance) {
        this.balanceOperationId = 0;
        this.balance = balance;
    }

    // Already existed Account Book
    public AccountBookClass(int balance, Map<Integer,SaleTransaction> SalOp, Map<Integer,Order> OrdOp, Map<Integer,ReturnTransaction> RetOp, Map<Integer,BalanceOperation> BalOp) {
        this.balanceOperationId = returnTransactionMap.size()+orderMap.size()+saleTransactionMap.size();
        this.balance = balance;
        this.saleTransactionMap.putAll(SalOp);
        this.returnTransactionMap.putAll(RetOp);
        this.orderMap.putAll(OrdOp);
        this.balanceOperationMap.putAll(BalOp);
    }


    @Override
    public Integer addSaleTransaction(SaleTransaction saleTransaction) throws InvalidTransactionIdException {
        Integer saleTransactionId = SaleTransaction.getId();
        balanceOperationId = id++;

        if(saleTransactionId == null ||  saleTransactionId <= 0)
            throw new InvalidTransactionIdException();

        balanceOperationMap.put(balanceOperationId, (BalanceOperation) saleTransaction);
        saleTransactionMap.put(saleTransactionId, saleTransaction);
    }

    @Override
    public Integer addReturnTransaction(ReturnTransaction returnTransaction) throws InvalidTransactionIdException {
        Integer returnTransactionId = returnTransaction.getId();
        balanceOperationId = balanceOperationMap.size() + 1;

        if (returnTransactionId == null || returnTransactionId <= 0)
            throw new InvalidTransactionIdException();

        balanceOperationMap.put(balanceOperationId, (BalanceOperation) returnTransaction);
        returnTransactionMap.put(returnTransactionId, returnTransaction);
    }

    @Override
    public Integer addOrder(Order order) throws InvalidTransactionIdException {
        Integer orderTransactionId = order.getId();
        balanceOperationId = balanceOperationMap.size() + 1;

        if (orderTransactionId == null || orderTransactionId <= 0)
            throw new InvalidTransactionIdException();

        balanceOperationMap.put(balanceOperationId, (BalanceOperation) order);
        orderMap.put(orderTransactionId, order);
    }

    @Override
    public Integer addTransaction(BalanceOperation balanceOperation) throws InvalidTransactionIdException {
        //id max
        balanceOperationMap.keySet()
        balanceOperationId = balanceOperationMap.size() + 1;

        if (balanceTransactionId <= 0)
            throw new InvalidTransactionIdException();

        balanceOperationMap.put(balanceOperationId, balanceOperation);

    }

    //In Design Transaction Objects are passed
    @Override
    public void removeSaleTransaction(Integer saleTransactionId) throws InvalidTransactionIdException {

        if(!saleTransactionMap.containsKey(saleTransactionId))
            throw new InvalidTransactionIdException();
        //Id si basa sulla dimensione del vettore, la cancellazione può eliminare un qualsiasi elemento, non per forza l'ultimo
        //Questo comporta che se togliamo il 5 elemento in un vettore di 10, il prossimo id sarà 10 ma c'è gia uno con chiave 10
        saleTransactionMap.remove(saleTransactionId);
    }

    @Override
    public void removeReturnTransaction(Integer returnTransactionId) throws InvalidTransactionIdException {
        if(!returnTransactionMap.containsKey(returnTransactionId))
            throw new InvalidTransactionIdException();

        returnTransactionMap.remove(returnTransactionId);
    }

    @Override
    public void removeOrder(Integer orderTransactionId) throws InvalidTransactionIdException {
        if(!orderMap.containsKey(orderTransactionId))
            throw new InvalidTransactionIdException();
        saleTransactionMap.remove(orderTransactionId);

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
}
