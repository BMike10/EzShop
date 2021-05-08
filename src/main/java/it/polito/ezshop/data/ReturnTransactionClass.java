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

    @Override
    public Map<Integer, ProductType> getReturnedProduct() {
        return returnedProduct;
    }

    @Override
    public void setReturnedProduct(Map<Integer, ProductType> returnedProduct) {
        this.returnedProduct = returnedProduct;
    }

    @Override
    public SaleTransaction getSaleTransaction() {
        return saleTransaction;
    }

    @Override
    public void setSaleTransaction(SaleTransaction saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    @Override
    public String getStatus() {
        return status.name();
    }

    @Override
    public void setStatus(String status) {
        this.status = ReturnStatus.valueOf(status);
    }

    //Account book???
    //    public void addReturnProduct(ProductType product,int quantity){
    //
    //    }

}
