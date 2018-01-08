package com.autobid.model;

import java.util.Date;

@SuppressWarnings("deprecation,unused")
public class DebtInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.debt_id
     *
     * @mbggenerated
     */
    private Integer debtId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.current_credit_code
     *
     * @mbggenerated
     */
    private String currentCreditCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.seller
     *
     * @mbggenerated
     */
    private String seller;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.status_id
     *
     * @mbggenerated
     */
    private Integer statusId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.lender
     *
     * @mbggenerated
     */
    private String lender;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.bid_date_time
     *
     * @mbggenerated
     */
    private String bidDateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.owing_number
     *
     * @mbggenerated
     */
    private Integer owingNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.owing_principal
     *
     * @mbggenerated
     */
    private Integer owingPrincipal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.owing_interest
     *
     * @mbggenerated
     */
    private String owingInterest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.days
     *
     * @mbggenerated
     */
    private Integer days;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.price_for_sale_rate
     *
     * @mbggenerated
     */
    private Integer priceForSaleRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.price_for_sale
     *
     * @mbggenerated
     */
    private Integer priceForSale;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.preference_degree
     *
     * @mbggenerated
     */
    private Integer preferenceDegree;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.listing_id
     *
     * @mbggenerated
     */
    private Integer listingId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.credit_code
     *
     * @mbggenerated
     */
    private String creditCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.listing_amount
     *
     * @mbggenerated
     */
    private Integer listingAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.listing_mouths
     *
     * @mbggenerated
     */
    private Integer listingMouths;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.listing_time
     *
     * @mbggenerated
     */
    private Date listingTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.listing_rate
     *
     * @mbggenerated
     */
    private Integer listingRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.past_due_number
     *
     * @mbggenerated
     */
    private Integer pastDueNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.allowance_radio
     *
     * @mbggenerated
     */
    private Integer allowanceRadio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column debt_info.PastDueDay
     *
     * @mbggenerated
     */
    private Integer pastdueday;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.debt_id
     *
     * @return the value of debt_info.debt_id
     *
     * @mbggenerated
     */
    public Integer getDebtId() {
        return debtId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.debt_id
     *
     * @param debtId the value for debt_info.debt_id
     *
     * @mbggenerated
     */
    public void setDebtId(Integer debtId) {
        this.debtId = debtId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.current_credit_code
     *
     * @return the value of debt_info.current_credit_code
     *
     * @mbggenerated
     */
    public String getCurrentCreditCode() {
        return currentCreditCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.current_credit_code
     *
     * @param currentCreditCode the value for debt_info.current_credit_code
     *
     * @mbggenerated
     */
    public void setCurrentCreditCode(String currentCreditCode) {
        this.currentCreditCode = currentCreditCode == null ? null : currentCreditCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.seller
     *
     * @return the value of debt_info.seller
     *
     * @mbggenerated
     */
    public String getSeller() {
        return seller;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.seller
     *
     * @param seller the value for debt_info.seller
     *
     * @mbggenerated
     */
    public void setSeller(String seller) {
        this.seller = seller == null ? null : seller.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.status_id
     *
     * @return the value of debt_info.status_id
     *
     * @mbggenerated
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.status_id
     *
     * @param statusId the value for debt_info.status_id
     *
     * @mbggenerated
     */
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.lender
     *
     * @return the value of debt_info.lender
     *
     * @mbggenerated
     */
    public String getLender() {
        return lender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.lender
     *
     * @param lender the value for debt_info.lender
     *
     * @mbggenerated
     */
    public void setLender(String lender) {
        this.lender = lender == null ? null : lender.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.bid_date_time
     *
     * @return the value of debt_info.bid_date_time
     *
     * @mbggenerated
     */
    public String getBidDateTime() {
        return bidDateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.bid_date_time
     *
     * @param bidDateTime the value for debt_info.bid_date_time
     *
     * @mbggenerated
     */
    public void setBidDateTime(String bidDateTime) {
        this.bidDateTime = bidDateTime == null ? null : bidDateTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.owing_number
     *
     * @return the value of debt_info.owing_number
     *
     * @mbggenerated
     */
    public Integer getOwingNumber() {
        return owingNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.owing_number
     *
     * @param owingNumber the value for debt_info.owing_number
     *
     * @mbggenerated
     */
    public void setOwingNumber(Integer owingNumber) {
        this.owingNumber = owingNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.owing_principal
     *
     * @return the value of debt_info.owing_principal
     *
     * @mbggenerated
     */
    public Integer getOwingPrincipal() {
        return owingPrincipal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.owing_principal
     *
     * @param owingPrincipal the value for debt_info.owing_principal
     *
     * @mbggenerated
     */
    public void setOwingPrincipal(Integer owingPrincipal) {
        this.owingPrincipal = owingPrincipal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.owing_interest
     *
     * @return the value of debt_info.owing_interest
     *
     * @mbggenerated
     */
    public String getOwingInterest() {
        return owingInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.owing_interest
     *
     * @param owingInterest the value for debt_info.owing_interest
     *
     * @mbggenerated
     */
    public void setOwingInterest(String owingInterest) {
        this.owingInterest = owingInterest == null ? null : owingInterest.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.days
     *
     * @return the value of debt_info.days
     *
     * @mbggenerated
     */
    public Integer getDays() {
        return days;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.days
     *
     * @param days the value for debt_info.days
     *
     * @mbggenerated
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.price_for_sale_rate
     *
     * @return the value of debt_info.price_for_sale_rate
     *
     * @mbggenerated
     */
    public Integer getPriceForSaleRate() {
        return priceForSaleRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.price_for_sale_rate
     *
     * @param priceForSaleRate the value for debt_info.price_for_sale_rate
     *
     * @mbggenerated
     */
    public void setPriceForSaleRate(Integer priceForSaleRate) {
        this.priceForSaleRate = priceForSaleRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.price_for_sale
     *
     * @return the value of debt_info.price_for_sale
     *
     * @mbggenerated
     */
    public Integer getPriceForSale() {
        return priceForSale;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.price_for_sale
     *
     * @param priceForSale the value for debt_info.price_for_sale
     *
     * @mbggenerated
     */
    public void setPriceForSale(Integer priceForSale) {
        this.priceForSale = priceForSale;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.preference_degree
     *
     * @return the value of debt_info.preference_degree
     *
     * @mbggenerated
     */
    public Integer getPreferenceDegree() {
        return preferenceDegree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.preference_degree
     *
     * @param preferenceDegree the value for debt_info.preference_degree
     *
     * @mbggenerated
     */
    public void setPreferenceDegree(Integer preferenceDegree) {
        this.preferenceDegree = preferenceDegree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.listing_id
     *
     * @return the value of debt_info.listing_id
     *
     * @mbggenerated
     */
    public Integer getListingId() {
        return listingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.listing_id
     *
     * @param listingId the value for debt_info.listing_id
     *
     * @mbggenerated
     */
    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.credit_code
     *
     * @return the value of debt_info.credit_code
     *
     * @mbggenerated
     */
    public String getCreditCode() {
        return creditCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.credit_code
     *
     * @param creditCode the value for debt_info.credit_code
     *
     * @mbggenerated
     */
    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode == null ? null : creditCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.listing_amount
     *
     * @return the value of debt_info.listing_amount
     *
     * @mbggenerated
     */
    public Integer getListingAmount() {
        return listingAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.listing_amount
     *
     * @param listingAmount the value for debt_info.listing_amount
     *
     * @mbggenerated
     */
    public void setListingAmount(Integer listingAmount) {
        this.listingAmount = listingAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.listing_mouths
     *
     * @return the value of debt_info.listing_mouths
     *
     * @mbggenerated
     */
    public Integer getListingMouths() {
        return listingMouths;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.listing_mouths
     *
     * @param listingMouths the value for debt_info.listing_mouths
     *
     * @mbggenerated
     */
    public void setListingMouths(Integer listingMouths) {
        this.listingMouths = listingMouths;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.listing_time
     *
     * @return the value of debt_info.listing_time
     *
     * @mbggenerated
     */
    public Date getListingTime() {
        return listingTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.listing_time
     *
     * @param listingTime the value for debt_info.listing_time
     *
     * @mbggenerated
     */
    public void setListingTime(Date listingTime) {
        this.listingTime = listingTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.listing_rate
     *
     * @return the value of debt_info.listing_rate
     *
     * @mbggenerated
     */
    public Integer getListingRate() {
        return listingRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.listing_rate
     *
     * @param listingRate the value for debt_info.listing_rate
     *
     * @mbggenerated
     */
    public void setListingRate(Integer listingRate) {
        this.listingRate = listingRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.past_due_number
     *
     * @return the value of debt_info.past_due_number
     *
     * @mbggenerated
     */
    public Integer getPastDueNumber() {
        return pastDueNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.past_due_number
     *
     * @param pastDueNumber the value for debt_info.past_due_number
     *
     * @mbggenerated
     */
    public void setPastDueNumber(Integer pastDueNumber) {
        this.pastDueNumber = pastDueNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.allowance_radio
     *
     * @return the value of debt_info.allowance_radio
     *
     * @mbggenerated
     */
    public Integer getAllowanceRadio() {
        return allowanceRadio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.allowance_radio
     *
     * @param allowanceRadio the value for debt_info.allowance_radio
     *
     * @mbggenerated
     */
    public void setAllowanceRadio(Integer allowanceRadio) {
        this.allowanceRadio = allowanceRadio;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column debt_info.PastDueDay
     *
     * @return the value of debt_info.PastDueDay
     *
     * @mbggenerated
     */
    public Integer getPastdueday() {
        return pastdueday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column debt_info.PastDueDay
     *
     * @param pastdueday the value for debt_info.PastDueDay
     *
     * @mbggenerated
     */
    public void setPastdueday(Integer pastdueday) {
        this.pastdueday = pastdueday;
    }
}