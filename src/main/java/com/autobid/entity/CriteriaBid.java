package com.autobid.entity;

public class CriteriaBid {
    private int amount;
    private String criteriaName;
    private int level;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String print() {
        //System.out.println(this.getCriteriaName()+":"+this.getLevel()+","+this.getAmount());
        return this.getCriteriaName() + ":" + this.getLevel() + "," + this.getAmount();
    }
}
