package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.JSONUtil;
import net.sf.json.JSONObject;

@SuppressWarnings({"deprecation","unused"})
public class BeginProCriteria implements Criteria, Constants {

    private boolean criteriaBachelor;
    private boolean criteriaMaster;
    private boolean criteriaA, criteriaC, criteriaD;

    //educateValidate = loanInfoObj.getInt("EducateValidate");
    public void calc(JSONObject loanInfos, ConfBean cb) {
        int certificateValidate = (int) loanInfos.get("CertificateValidate");

        //educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
        String educationDegree = (String) loanInfos.get("EducationDegree");

        //studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
        String studyStyle = (String) loanInfos.get("StudyStyle");

        boolean criteriaCertificate = certificateValidate > 0;

        criteriaBachelor = JSONUtil.decodeUnicode(educationDegree).equals("����") &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����"));

        criteriaMaster = (JSONUtil.decodeUnicode(educationDegree).equals("˶ʿ") ||
                JSONUtil.decodeUnicode(educationDegree).equals("�о���")) &&
                (JSONUtil.decodeUnicode(studyStyle).equals("��ͨ") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("��ͨȫ����") ||
                        JSONUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));
        Integer loanAmount = (Integer) loanInfos.get("Amount");
        Integer highestPrincipal = (Integer) loanInfos.get("HighestPrincipal");
        Integer owingAmount = (Integer) loanInfos.get("OwingAmount");
        Integer highestDebt = (Integer) loanInfos.get("HighestDebt");
        Integer totalPrincipal = (Integer) loanInfos.get("TotalPrincipal");
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

    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
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
