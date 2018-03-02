package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JSONUtil;
import net.sf.json.JSONObject;

public class EduDebtProCriteria implements Criteria, Constants {

    private double debtTotalRate;
    private boolean criteriaBachelor;
    private boolean criteriaMaster;

    public void calc(JSONObject loanInfos, ConfBean cb) {
        double totalPrincipal = Double.parseDouble(loanInfos.get("TotalPrincipal").toString());
        double owingAmount = Double.parseDouble(loanInfos.get("OwingAmount").toString());
        double loanAmount = Double.parseDouble(loanInfos.get("Amount").toString());

        debtTotalRate = totalPrincipal != 0 ? (owingAmount + loanAmount) / totalPrincipal : 1;

        //educationDegree 包括：本科、专科、硕士、研究生、专升本、专科（高职）
        String educationDegree = (String) loanInfos.get("EducationDegree");

        //studyStyle 包括：函授、开放教育、成人、普通、普通全日制、研究生、网络教育、脱产、自学考试、自考
        String studyStyle = (String) loanInfos.get("StudyStyle");

        criteriaBachelor = JSONUtil.decodeUnicode(educationDegree).equals("本科") &&
                (JSONUtil.decodeUnicode(studyStyle).equals("普通") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("普通全日制"));

        criteriaMaster = (JSONUtil.decodeUnicode(educationDegree).equals("硕士") ||
                JSONUtil.decodeUnicode(educationDegree).equals("研究生")) &&
                (JSONUtil.decodeUnicode(studyStyle).equals("普通") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("普通全日制") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("脱产"));
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
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
