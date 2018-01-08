package com.autobid.entity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class DebtListResult {
    private ArrayList<String> debtList;
    private int indexNum;
    private int debtIdCount;

    public ArrayList<String> getDebtList() {
        return debtList;
    }

    public void setDebtList(ArrayList<String> debtList) {
        this.debtList = debtList;
    }

    public int getDebtIdCount() {
        return debtIdCount;
    }

    public void setDebtIdCount(int debtIdCount) {
        this.debtIdCount = debtIdCount;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }
}