package it.polito.ezshop.data;

import java.time.LocalDate;

public class BalanceOperationClass implements BalanceOperation {

    //Our Design
    private static int nBalance = 0;
    private int id;
    private String description;
    private double amount;
    private LocalDate date;
    //Other
    private String type;

    public BalanceOperationClass(double amount, String type) {
        this.id = nBalance++;
        this.description = "";
        this.amount = amount;
        this.date = LocalDate.now();
        this.type = type;
    }

    public BalanceOperationClass(int orderId, String description, double amount, LocalDate date, String type) {
        this.id = orderId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    @Override
    public int getBalanceId() {
        return this.id;
    }

    @Override
    public void setBalanceId(int balanceId) {
        this.id = balanceId;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public double getMoney() {
        return this.amount;
    }

    @Override
    public void setMoney(double money) {
        this.amount = money;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String desc) {
        this.description = desc;
    }
}
