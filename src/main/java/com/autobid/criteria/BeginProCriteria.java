package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JsonUtil;

import java.util.HashMap;

public class BeginProCriteria implements Criteria, Constants {

    private boolean criteriaBachelor;
    private boolean criteriaMaster;
    private boolean criteriaA, criteriaC, criteriaD;

    //educateValidate = loanInfoObj.getInt("EducateValidate");
    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {
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
        Integer loanAmount = (Integer) loanInfoMap.get("Amount");
        Integer highestPrincipal = (Integer) loanInfoMap.get("HighestPrincipal");
        Integer owingAmount = (Integer) loanInfoMap.get("OwingAmount");
        Integer highestDebt = (Integer) loanInfoMap.get("HighestDebt");
        Integer totalPrincipal = (Integer) loanInfoMap.get("TotalPrincipal");
        //System.out.println("loanAmount:"+loanAmount);
        criteriaA = loanAmount >= Integer.parseInt(cb.getAmountBegin()) &&
                loanAmount <= Integer.parseInt(cb.getAmountEnd()) &&
                totalPrincipal >= Integer.parseInt(cb.getTotalLimit()) &&
                loanAmount < highestPrincipal;
/*		criteriaB = owingAmount + loanAmount < highestDebt && 
				owingAmount < Integer.parseInt(ConfUtil.getProperty("owing_limit")) ;*/
        criteriaC = owingAmount == 0;
        criteriaD = owingAmount < highestDebt / 2 && loanAmount < highestDebt / 2;
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if (criteriaMaster && criteriaA && criteriaC && criteriaD) {
            return PERFECT;
        } else if (criteriaBachelor && criteriaA && criteriaC && criteriaD) {
            return GOOD;
        } else if (criteriaBachelor || (criteriaA && criteriaC && criteriaD)) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "BeginPro";
    }
}
