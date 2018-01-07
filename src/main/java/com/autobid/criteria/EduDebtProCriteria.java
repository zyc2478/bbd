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

        //educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
        String educationDegree = (String) loanInfoMap.get("EducationDegree");

        //studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
        String studyStyle = (String) loanInfoMap.get("StudyStyle");

        boolean criteriaCertificate = certificateValidate > 0;

        criteriaBachelor = JsonUtil.decodeUnicode(educationDegree).equals("����") &&
                (JsonUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("��ͨȫ����"));

        criteriaMaster = (JsonUtil.decodeUnicode(educationDegree).equals("˶ʿ") ||
                JsonUtil.decodeUnicode(educationDegree).equals("�о���")) &&
                (JsonUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("��ͨȫ����") ||
                        JsonUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));
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
