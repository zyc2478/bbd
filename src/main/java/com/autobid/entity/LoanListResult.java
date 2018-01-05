package com.autobid.entity;

import net.sf.json.JSONArray;

public class LoanListResult {
    private JSONArray loanList;
    private int indexNum;
    private int loanIdCount;

    public JSONArray getLoanList() {
        return loanList;
    }

    public void setLoanList(JSONArray loanList) {
        this.loanList = loanList;
    }

    public int getLoanIdCount() {
        return loanIdCount;
    }

    public void setLoanIdCount(int loanIdCount) {
        this.loanIdCount = loanIdCount;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }
}
