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

        //educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
        String educationDegree = (String) loanInfos.get("EducationDegree");

        //studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
        String studyStyle = (String) loanInfos.get("StudyStyle");

        criteriaBachelor = JSONUtil.decodeUnicode(educationDegree).equals("����") &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����"));

        criteriaMaster = (JSONUtil.decodeUnicode(educationDegree).equals("˶ʿ") ||
                JSONUtil.decodeUnicode(educationDegree).equals("�о���")) &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));
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
