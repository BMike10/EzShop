package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReturnTransactionClass extends BalanceOperationClass implements ReturnTransaction{

    //Map with the barcode and quantity of product in the sale transaction
    private final Map<ProductType,Integer> returnedProduct = new HashMap<>();
    private SaleTransaction saleTransaction;
    private ReturnStatus status;

    public ReturnTransactionClass(double amount, String type, Map<ProductType,Integer> returned, SaleTransaction saleT, ReturnStatus retstatus) {
        super(amount, type);
        this.returnedProduct.putAll(returned);
        this.saleTransaction = saleT;
        this.status = retstatus;
    }

    public ReturnTransactionClass(int orderId, String description, double amount, LocalDate date, String type,Map<ProductType,Integer> returned, SaleTransaction saleT, ReturnStatus retstatus) {
        super(orderId, description, amount, date, type);
        this.returnedProduct.putAll(returned);
        this.saleTransaction = saleT;
        this.status = retstatus;
    }

    @Override
    public Integer getReturnId() {
        return super.getBalanceId();
    }

    @Override
    public void setReturnId(Integer balanceId) {
        super.setBalanceId(balanceId);
    }

    @Override
    public Map<ProductType,Integer> getReturnedProduct() {
        return returnedProduct;
    }

    @Override
    public void setReturnedProduct(Map<ProductType,Integer> returnedProduct) {
        this.returnedProduct.clear();
        this.returnedProduct.putAll(returnedProduct);
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
