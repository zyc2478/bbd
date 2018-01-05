package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.io.IOException;
import java.util.HashMap;

public class CreditCodeCriteria implements Criteria, Constants {

    final int creditAA = 0;
    final int creditA = 1;
    final int creditB = 2;
    final int creditC = 3;
    final int creditD = 4;
    final int creditE = 5;
    final int creditF = 6;
    String creditCode, creditCodeLimit;
    int credit, creditLimit;
    boolean criteriaCredit;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws IOException {
        creditCode = (String) loanInfoMap.get("CreditCode");
        creditCodeLimit = cb.getCreditLimit();
        switch (creditCode) {
            case "AA":
                credit = creditAA;
                break;
            case "A":
                credit = creditA;
                break;
            case "B":
                credit = creditB;
                break;
            case "C":
                credit = creditC;
                break;
            case "D":
                credit = creditD;
                break;
            case "E":
                credit = creditE;
                break;
            case "F":
                credit = creditF;
                break;
        }
        switch (creditCodeLimit) {
            case "AA":
                creditLimit = creditAA;
                break;
            case "A":
                creditLimit = creditA;
                break;
            case "B":
                creditLimit = creditB;
                break;
            case "C":
                creditLimit = creditC;
                break;
            case "D":
                creditLimit = creditD;
                break;
            case "E":
                creditLimit = creditE;
                break;
            case "F":
                creditLimit = creditF;
                break;
        }
        //System.out.println("credit:" + credit);
        //System.out.println("creditLimit:" + creditLimit);
        criteriaCredit = credit >= creditLimit;
        //criteriaCredit = true;
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws IOException {
        calc(loanInfoMap, cb);
        if (criteriaCredit) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "CreditCode";
    }
}
