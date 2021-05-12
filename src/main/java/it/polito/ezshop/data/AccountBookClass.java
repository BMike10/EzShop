package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import sun.util.resources.LocaleData;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AccountBookClass implements AccountBook{
    //Our Design

    private double balance;
    private final Map<Integer,SaleTransaction> saleTransactionMap = new HashMap<>();
    private final Map<Integer,Order> orderMap = new HashMap<>();
    private final Map<Integer,ReturnTransaction> returnTransactionMap = new HashMap<>();
    private final Map<Integer,BalanceOperation> balanceOperationMap = new HashMap<>();

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
        //saleTransaction.setSaleId(newId);
        this.balanceOperationMap.put(newId, (BalanceOperation) saleTransaction);
        this.saleTransactionMap.put(newId, saleTransaction);
        return newId;
    }

    @Override
    public Integer addReturnTransaction(ReturnTransaction returnTransaction) {
        Integer newId = newId();
        returnTransaction.setReturnId(newId);
        this.balanceOperationMap.put(newId, (BalanceOperation) returnTransaction);
        this.returnTransactionMap.put(newId, returnTransaction);
        return newId;

    }

    @Override
    public Integer addOrder(Order order){
        Integer newId = newId();
        order.setOrderId(newId);
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

    public void setSaleTransactionMap(Map<Integer,SaleTransaction> newSaleMap){
        this.saleTransactionMap.clear();
        this.saleTransactionMap.putAll(newSaleMap);
    }

    public void setOrderMap(Map<Integer,Order> newOrderMap){
        this.orderMap.clear();
        this.orderMap.putAll(newOrderMap);
    }

    public void setReturnTransactionMap(Map<Integer,ReturnTransaction> newReturnMap){
        this.returnTransactionMap.clear();
        this.returnTransactionMap.putAll(newReturnMap);
    }

    public List<BalanceOperation> getBalanceOperationByDate(LocalDate from, LocalDate to){
        List<BalanceOperation> bo;
        if(from==null && to!=null){
            //All Balance operation from start to LocalDateTo
            bo = balanceOperationMap.values().stream().
                    filter(t -> (t.getDate().isBefore(to))).collect(Collectors.toList());
        }else if(from!=null && to==null){
            //All Balance operation from LocalDateFrom to end
            bo = balanceOperationMap.values().stream().
                    filter(t -> (t.getDate().isAfter(from))).collect(Collectors.toList());
        }else if (from==null){
            //All Balance operation -> to==null(if it's not -> first if)
            bo = new ArrayList<>(balanceOperationMap.values());
        }else{
            bo = balanceOperationMap.values().stream().
                    filter(t -> (t.getDate().isAfter(from))).filter(t -> (t.getDate().isBefore(to))).collect(Collectors.toList());
        }
        return bo;
    }


    public Integer newId(){
        Integer newKey = null;

        if(balanceOperationMap.isEmpty())
            return 1;
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
