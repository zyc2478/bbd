package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class CertificateValidateCriteria implements Criteria, Constants {

    private boolean criteriaCertificate;

    public void calc(JSONObject loanInfos, ConfBean confBean) {
        int certificate_config = Integer.parseInt(loanInfos.get("CertificateValidate").toString());
        int certificate = Integer.parseInt(confBean.getCertificateValidate());
        if(certificate  == certificate_config || certificate_config == 2){
            criteriaCertificate = true;
        }else{
            criteriaCertificate = false;
        }
    }

    public int getLevel(JSONObject loanInfos, ConfBean confBean) {
        calc(loanInfos, confBean);
        if (criteriaCertificate) {
            return OK;
        } else {
            return NONE;
        }
    }

    @Override
    public String getCriteriaName() {
        return "CertificateValidate";
    }

}
