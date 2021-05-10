package it.polito.ezshop.data;

import java.util.Map;

public interface ReturnTransaction {
    Map<Integer, ProductType> getReturnedProduct();

    void setReturnedProduct(Map<Integer, ProductType> returnedProduct) ;

    SaleTransaction getSaleTransaction();

    void setSaleTransaction(SaleTransaction saleTransaction);

    String getStatus() ;

    void setStatus(String status);
}