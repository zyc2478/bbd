package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JsonUtil;

import java.util.HashMap;

public class EducationProCriteria implements Criteria, Constants {

    private boolean criteriaBachelor;
    private boolean criteriaMaster;

    //educateValidate = loanInfoObj.getInt("EducateValidate");
    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {

        //educationDegree 包括：本科、专科、硕士、研究生、专升本、专科（高职）
        String educationDegree = (String) loanInfoMap.get("EducationDegree");

        //studyStyle 包括：函授、开放教育、成人、普通、普通全日制、研究生、网络教育、脱产、自学考试、自考
        String studyStyle = (String) loanInfoMap.get("StudyStyle");

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
        if (criteriaMaster) {
            return PERFECT;
        } else if (criteriaBachelor) {
            return GOOD;
        } else return NONE;
    }

    public String getCriteriaName() {
        return "EducationPro";
    }
}
