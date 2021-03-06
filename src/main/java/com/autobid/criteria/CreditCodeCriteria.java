package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

public class CreditCodeCriteria implements Criteria, Constants {

    private int credit,creditLimit,creditRange;
    private boolean criteriaCredit;

    public void calc(JSONObject loanInfos, ConfBean cb) {
        String creditCodeLimit = cb.getCreditLimit();
        String creditCode = (String) loanInfos.get("CreditCode");
        creditRange = Integer.parseInt(cb.getCreditRange());
        credit = switchCredit(creditCode);
        creditLimit = switchCredit(creditCodeLimit);

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

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
        if(creditRange==1 && credit >= creditLimit){
            return OK;
        }else if(creditRange==0 && credit == creditLimit){
            return OK;
        }else{
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "CreditCode";
    }
}
