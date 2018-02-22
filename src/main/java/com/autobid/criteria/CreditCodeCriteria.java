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
        String creditCodeLimit = cb.getCreditLimit();
        String creditCode = (String) loanInfoMap.get("CreditCode");
        credit = switchCredit(creditCode);
        creditLimit = switchCredit(creditCodeLimit);
        criteriaCredit = credit >= creditLimit;
    }
   public int switchCredit(String creditCode){
        int codeNum = 0;
        switch (creditCode) {
            case "AA":
                codeNum = 0;
                break;
            case "A":
                codeNum = 1;
                break;
            case "B":
                codeNum = 2;
                break;
            case "C":
                codeNum = 3;
                break;
            case "D":
                codeNum = 4;
                break;
            case "E":
                codeNum = 5;
                break;
            case "F":
                codeNum = 6;
                break;
        }
        return codeNum;
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
