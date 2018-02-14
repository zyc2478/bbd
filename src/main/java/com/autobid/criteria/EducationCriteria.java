package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JSONUtil;

import java.util.HashMap;


public class EducationCriteria implements Criteria, Constants {

    private boolean criteriaCertificate, criteriaBachelor, criteriaMaster;

    //educateValidate = loanInfoObj.getInt("EducateValidate");
    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) {

        int certificateValidate = (int) loanInfoMap.get("CertificateValidate");

        //educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
        String educationDegree = (String) loanInfoMap.get("EducationDegree");

        //studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
        String studyStyle = (String) loanInfoMap.get("StudyStyle");

        criteriaCertificate = certificateValidate > 0;

        criteriaBachelor = JSONUtil.decodeUnicode(educationDegree).equals("����") &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����"));

        criteriaMaster = (JSONUtil.decodeUnicode(educationDegree).equals("˶ʿ") ||
                JSONUtil.decodeUnicode(educationDegree).equals("�о���")) &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));

    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) {
        calc(loanInfoMap, cb);
        if (criteriaMaster) {
            return PERFECT;
        } else if (criteriaBachelor) {
            return GOOD;
        } else if (criteriaCertificate) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "Education";
    }
}
