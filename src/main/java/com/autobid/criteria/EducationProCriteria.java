package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.FormatUtil;
import com.autobid.util.JSONUtil;
import net.sf.json.JSONObject;

public class EducationProCriteria implements Criteria, Constants {

    private boolean criteriaBachelor;
    private boolean criteriaMaster;

    //educateValidate = loanInfoObj.getInt("EducateValidate");
    public void calc(JSONObject loanInfos, ConfBean cb) {

        //educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
        String educationDegree = FormatUtil.nullToStr(loanInfos.get("EducationDegree")) ;

        //studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
        String studyStyle = FormatUtil.nullToStr(loanInfos.get("StudyStyle"));

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
