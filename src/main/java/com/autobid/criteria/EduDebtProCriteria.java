package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JsonUtil;

import java.util.HashMap;

public class EduDebtProCriteria implements Criteria, Constants {

    private double debtTotalRate;
    boolean criteriaDebtRate;

    private boolean criteriaBachelor;
    private boolean criteriaMaster;

    SuccessCountCriteria successCountCriteria = new SuccessCountCriteria();

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        double totalPrincipal = Double.parseDouble(loanInfoMap.get("TotalPrincipal").toString());
        double owingAmount = Double.parseDouble(loanInfoMap.get("OwingAmount").toString());
        double loanAmount = Double.parseDouble(loanInfoMap.get("Amount").toString());

        debtTotalRate = totalPrincipal != 0 ? (owingAmount + loanAmount) / totalPrincipal : 1;

        int certificateValidate = (int) loanInfoMap.get("CertificateValidate");

        //educationDegree 包括：本科、专科、硕士、研究生、专升本、专科（高职）
        String educationDegree = (String) loanInfoMap.get("EducationDegree");

        //studyStyle 包括：函授、开放教育、成人、普通、普通全日制、研究生、网络教育、脱产、自学考试、自考
        String studyStyle = (String) loanInfoMap.get("StudyStyle");

        boolean criteriaCertificate = certificateValidate > 0;

        criteriaBachelor = JsonUtil.decodeUnicode(educationDegree).equals("本科") &&
                (JsonUtil.decodeUnicode(studyStyle).equals("普通") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("普通全日制"));

        criteriaMaster = (JsonUtil.decodeUnicode(educationDegree).equals("硕士") ||
                JsonUtil.decodeUnicode(educationDegree).equals("研究生")) &&
                (JsonUtil.decodeUnicode(studyStyle).equals("普通") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("普通全日制") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("脱产"));
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if (debtTotalRate < 0.25 && criteriaMaster) {
            return PERFECT;
        } else if (debtTotalRate < 0.33 && criteriaMaster) {
            return GOOD;
        } else if (debtTotalRate < 0.25 && criteriaBachelor) {
            return OK;
        }
        return NONE;
    }

    public String getCriteriaName() {
        return "EduDebtPro";
    }
}
