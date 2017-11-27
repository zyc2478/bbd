package com.autobid.entity;

import java.util.ArrayList;

public class LoanListResult {
	private ArrayList<String> loanList;
	private int indexNum;
	private int loanIdCount;
	public ArrayList<String> getLoanList() {
		return loanList;
	}
	public int getLoanIdCount() {
		return loanIdCount;
	}
	public void setLoanIdCount(int loanIdCount) {
		this.loanIdCount = loanIdCount;
	}
	public void setLoanList(ArrayList<String> loanList) {
		this.loanList = loanList;
	}
	public int getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}
}
