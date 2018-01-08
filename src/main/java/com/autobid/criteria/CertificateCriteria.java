package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;
@SuppressWarnings("deprecation")
public class CertificateCriteria implements Criteria, Constants {

    private int certificateSum;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        int certificateValidate = Integer.parseInt(loanInfoMap.get("CertificateValidate").toString());
        int nciicIdentityCheck = Integer.parseInt(loanInfoMap.get("NciicIdentityCheck").toString());
        int creditValidate = Integer.parseInt(loanInfoMap.get("CreditValidate").toString());
        int videoValidate = Integer.parseInt(loanInfoMap.get("VideoValidate").toString());

        certificateSum = certificateValidate + nciicIdentityCheck + creditValidate + videoValidate;
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        switch (certificateSum) {
            case 2:
                return OK;
            case 3:
                return GOOD;
            case 4:
                return PERFECT;
            default:
                return NONE;
        }
    }

    public String getCriteriaName() {
        return "Certificate";
    }

}
