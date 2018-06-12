package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class CertificateCheckCriteria implements Criteria, Constants {

    int certificate, certiCheckConfig;

    public void calc(JSONObject loanInfos, ConfBean confBean) {
        certiCheckConfig = Integer.parseInt(loanInfos.get("CertificateValidate").toString());
        certificate = Integer.parseInt(confBean.getCertificateCheck());
    }

    public int getLevel(JSONObject loanInfos, ConfBean confBean) {
        calc(loanInfos, confBean);
        if(certificate  == 1 || certiCheckConfig ==0){
            return  OK;
        }else{
            return NONE;
        }
    }

    @Override
    public String getCriteriaName() {
        return "CertificateValidate";
    }

}
