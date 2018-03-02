package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class CertificateCriteria implements Criteria, Constants {

    private int certificateSum;

    public void calc(JSONObject loanInfos, ConfBean cb) {
        int certificateValidate = Integer.parseInt(loanInfos.get("CertificateValidate").toString());
        int nciicIdentityCheck = Integer.parseInt(loanInfos.get("NciicIdentityCheck").toString());
        int creditValidate = Integer.parseInt(loanInfos.get("CreditValidate").toString());
        int videoValidate = Integer.parseInt(loanInfos.get("VideoValidate").toString());

        certificateSum = certificateValidate + nciicIdentityCheck + creditValidate + videoValidate;
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
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
