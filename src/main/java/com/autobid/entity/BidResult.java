package com.autobid.entity;

@SuppressWarnings("deprecation")
public class BidResult {
    private Integer bidId;
    private Integer bidAmount;

    public BidResult(Integer bidId, Integer bidAmount) {
        this.bidId = bidId;
        this.bidAmount = bidAmount;
    }

    public Integer getBidId() {
        return this.bidId;
    }

    public Integer getBidAmount() {
        return this.bidAmount;
    }

}
