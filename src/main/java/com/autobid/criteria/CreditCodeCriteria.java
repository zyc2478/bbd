package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

public class CreditCodeCriteria implements Criteria, Constants {

    private int credit;
    private int creditLimit;
    private boolean criteriaCredit;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        String creditCode = (String) loanInfoMap.get("CreditCode");
        credit = switchCredit(credit,creditCode);
        creditLimit = switchCredit(creditLimit,creditCode);
        criteriaCredit = credit >= creditLimit;
    }
   public int switchCredit(int credit, String creditCode){
        switch (creditCode) {
            case "AA":
                credit = 0;
                break;
            case "A":
                credit = 1;
                break;
            case "B":
                credit = 2;
                break;
            case "C":
                credit = 3;
                break;
            case "D":
                credit = 4;
                break;
            case "E":
                credit = 5;
                break;
            case "F":
                credit = 6;
                break;
        }
        return credit;
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
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
