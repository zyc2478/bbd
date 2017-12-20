package com.autobid.entity;

public class DebtInfo {
	
	private int DebtId;
	private int owingNumber;
	private double priceForSaleRate;
	private double priceForSale;
	private int listingId;
	private String creditCode;
	
	public int getDebtId() {
		return DebtId;
	}
	public void setDebtId(int debtId) {
		DebtId = debtId;
	}
	public int getOwingNumber() {
		return owingNumber;
	}
	public void setOwingNumber(int owingNumber) {
		this.owingNumber = owingNumber;
	}
	public double getPriceForSaleRate() {
		return priceForSaleRate;
	}
	public void setPriceForSaleRate(double priceForSaleRate) {
		this.priceForSaleRate = priceForSaleRate;
	}
	public double getPriceForSale() {
		return priceForSale;
	}
	public void setPriceForSale(double priceForSale) {
		this.priceForSale = priceForSale;
	}
	public int getListingId() {
		return listingId;
	}
	public void setListingId(int listingId) {
		this.listingId = listingId;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
}
