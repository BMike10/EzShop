package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;

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
        if(balance>=0)
            this.balance = balance;
        else this.balance = 0;
    }

    // Already existed Account Book
    @SuppressWarnings("unchecked")
    public AccountBookClass(Map<Integer,SaleTransaction> SalOp, Map<Integer,Order> OrdOp, Map<Integer,ReturnTransaction> RetOp) {

        this.saleTransactionMap.putAll(SalOp);
        this.orderMap.putAll(OrdOp);
        this.returnTransactionMap.putAll(RetOp);

        for (Map.Entry<Integer, Order> entry : orderMap.entrySet()) {
            balanceOperationMap.put(entry.getKey(), (BalanceOperation) entry.getValue());
        }
        for (Map.Entry<Integer, ReturnTransaction> entry : returnTransactionMap.entrySet()) {
            balanceOperationMap.put(entry.getKey(), (BalanceOperation) entry.getValue());
        }
        for (Map.Entry<Integer, SaleTransaction> entry : saleTransactionMap.entrySet()) {
            balanceOperationMap.put(entry.getKey(), (BalanceOperation) entry.getValue());
        }

        double CREDIT = this.saleTransactionMap.values().stream().
                filter(saleTransaction -> ((SaleTransactionClass)saleTransaction).getStatus().toString().equals("PAYED")).
                mapToDouble(SaleTransaction::getPrice).sum();
        double DEBIT = this.orderMap.values().stream().
                filter(order -> ((OrderClass)order).getStatus().equals("PAYED")).
                mapToDouble(order -> ((OrderClass) order).getMoney()).sum();
        this.balance = CREDIT -DEBIT;

        // SET INITIAL BALANCE -> Non tengo conto lo stato delle transazioni
//        if(!balanceOperationMap.isEmpty()){
//            double newBalance;
//            //IS BALANCE MONEY VALUE ALWAYS SET TO A CORRECT VALUE?
//            //It's works if the saleTransaction doesn't update after returnTransaction
//            double CREDIT = this.balanceOperationMap.values().stream().
//                    filter(balanceOperation -> balanceOperation.getType().equals("CREDIT")).mapToDouble(BalanceOperation::getMoney).sum();
//            double DEBIT = this.balanceOperationMap.values().stream().
//                    filter(balanceOperation -> ((BalanceOperationClass)balanceOperation).getDescription().equals("ORDER")).mapToDouble(BalanceOperation::getMoney).sum();
//            newBalance = CREDIT - DEBIT;
//            this.balance = newBalance;

    }

    @Override
    public Integer addSaleTransaction(SaleTransaction saleTransaction) {
        //Sale Transaction is complete but without id
        Integer newId = newId();
        saleTransaction.setTicketNumber(newId);
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

        if(!this.saleTransactionMap.containsKey(saleTransactionId) || saleTransactionId==null || saleTransactionId<=0)
            throw new InvalidTransactionIdException();

        this.saleTransactionMap.remove(saleTransactionId);
        this.balanceOperationMap.remove(saleTransactionId);
    }

    @Override
    public void removeReturnTransaction(Integer returnTransactionId) throws InvalidTransactionIdException {
        if(!this.returnTransactionMap.containsKey(returnTransactionId) || returnTransactionId==null || returnTransactionId<=0)
            throw new InvalidTransactionIdException();

        this.returnTransactionMap.remove(returnTransactionId);
        this.balanceOperationMap.remove(returnTransactionId);
    }

    @Override
    public void removeOrder(Integer orderTransactionId) throws InvalidTransactionIdException {
        if(orderTransactionId==null || orderTransactionId<=0 || !this.orderMap.containsKey(orderTransactionId) )
            throw new InvalidTransactionIdException();
        this.saleTransactionMap.remove(orderTransactionId);
        this.balanceOperationMap.remove(orderTransactionId);
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer id) {
        if(id==null || id<=0 || !this.saleTransactionMap.containsKey(id)  )
            throw new RuntimeException(new InvalidTransactionIdException());

        return this.saleTransactionMap.get(id);
    }

    @Override
    public ReturnTransaction getReturnTransaction(Integer id)  {
        if(!this.returnTransactionMap.containsKey(id) || id==null || id<=0)
            throw new RuntimeException(new InvalidTransactionIdException());

        return this.returnTransactionMap.get(id);
    }

    @Override
    public Order getOrder(Integer id)  {
        if(!this.orderMap.containsKey(id) || id==null || id<=0)
            throw new RuntimeException(new InvalidTransactionIdException());
            return this.orderMap.get(id);
    }

    @Override
    public Double getBalance() {
        return this.balance;
    }

    public boolean setBalance(double balance) {
        if(balance<0)
            return false;
        this.balance = balance;
        return true;
    }

    public Map<Integer,SaleTransaction> getSaleTransactionMap(){
        return this.saleTransactionMap;
    }

    public Map<Integer,Order> getOrderMap(){
        return this.orderMap;
    }

    public Map<Integer,ReturnTransaction> getReturnTransactionMap(){
        return this.returnTransactionMap;
    }

    public Map<Integer,BalanceOperation> getBalanceOperationMap(){
        return this.balanceOperationMap;
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
        }else {
            System.out.println("Ciao");
            bo = balanceOperationMap.values().stream().
                    filter(t -> t.getDate().isBefore(to) && t.getDate().isAfter(from)).collect(Collectors.toList());
        }
        return bo;
    }


    public Integer newId(){
        return balanceOperationMap.keySet().stream().max(Comparator.comparingInt(t->t)).orElse(0) + 1;
    }
}
