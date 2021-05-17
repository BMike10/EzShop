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
        try {
			super.setDescription("RETURN");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public ReturnTransactionClass(int orderId, String description, double amount, LocalDate date, String type,Map<ProductType,Integer> returned, SaleTransaction saleT, ReturnStatus retstatus) {
        super(orderId, description, amount, date, type);
        this.returnedProduct.putAll(returned);
        this.saleTransaction = saleT;
        this.status = retstatus;
    }

    public ReturnTransactionClass(SaleTransaction saleT, ReturnStatus retstatus) {
    	super(0.0, "DEBIT");
        this.saleTransaction = saleT;
        this.status = retstatus;
        try {
			super.setDescription("RETURN");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

    @Override
    public double getMoney() {
        return super.getMoney();
    }
    @Override
    public void setMoney(double money) {
        super.setMoney(money);
    }

    public int addReturnProduct(ProductType product, int quantity){
        SaleTransactionClass st=(SaleTransactionClass)this.saleTransaction;
        int amount=st.getTicketEntries().get(product.getBarCode()).getAmount();

        if(!this.returnedProduct.keySet().contains(product)) {
            if(amount<quantity) return -1;
            this.returnedProduct.put(product, quantity);
            setMoney(getMoney()+product.getPricePerUnit()*quantity*(1-st.getProductsEntries().get(product.getBarCode()).getDiscountRate()));
            //st.getProductsEntries().get(product.getBarCode()).setAmount(amount-quantity);
            return 1;
        }
        else { //if it's not the first return transaction for that product
            int q=this.returnedProduct.get(product);
            if(amount<quantity+q) return -1;
            this.returnedProduct.remove(product);
            this.returnedProduct.put(product, quantity+q);
            setMoney(getMoney()+product.getPricePerUnit()*quantity*(1-st.getProductsEntries().get(product.getBarCode()).getDiscountRate()));
            //st.getProductsEntries().get(product.getBarCode()).setAmount(amount-quantity+q);
            return 1;
        }
    }


}
