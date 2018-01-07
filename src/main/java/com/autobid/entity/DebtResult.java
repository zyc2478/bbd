package com.autobid.entity;

public class DebtResult {
    private int debtId;
    private int listingId;
    private double price;

    public DebtResult(int debtId, int listingId, double price) {
        this.setDebtId(debtId);
        this.setListingId(listingId);
        this.setPrice(price);
    }

    public int getDebtId() {
        return debtId;
    }

    private void setDebtId(int debtId) {
        this.debtId = debtId;
    }

    public int getListingId() {
        return listingId;
    }

    private void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public double getPrice() {
        return price;
    }

    private void setPrice(double price) {
        this.price = price;
    }
}
