package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.Map;

public class ReturnTransactionClass extends BalanceOperationClass implements ReturnTransaction{

    private Map<Integer,ProductType> returnedProduct;
    private SaleTransaction saleTransaction;
    private ReturnStatus status;

    public ReturnTransactionClass(double amount, String type, Map<Integer,ProductType> returned, SaleTransaction saleT, ReturnStatus retstatus) {
        super(amount, type);
        this.returnedProduct.putAll(returned);
        this.saleTransaction = saleT;
        this.status = retstatus;
    }

    public ReturnTransactionClass(int orderId, String description, double amount, LocalDate date, String type,Map<Integer,ProductType> returned, SaleTransaction saleT, ReturnStatus retstatus) {
        super(orderId, description, amount, date, type);
        this.returnedProduct.putAll(returned);
        this.saleTransaction = saleT;
        this.status = retstatus;
    }

    public Map<Integer, ProductType> getReturnedProduct() {
        return returnedProduct;
    }

    public void setReturnedProduct(Map<Integer, ProductType> returnedProduct) {
        this.returnedProduct = returnedProduct;
    }

    public SaleTransaction getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(SaleTransaction saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public ReturnStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnStatus status) {
        this.status = status;
    }
    //Account book???
//    public void addReturnProduct(ProductType product,int quantity){
//
//    }

}
