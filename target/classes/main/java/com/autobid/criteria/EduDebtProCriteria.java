package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.JsonUtil;

public class EduDebtProCriteria implements Criteria,Constants {

	double debtTotalRate,totalPrincipal,owingAmount,loanAmount;
	boolean criteriaDebtRate;

	int certificateValidate;
	String educationDegree,studyStyle;
	boolean criteriaCertificate,criteriaBachelor,criteriaMaster;
	
	SuccessCountCriteria successCountCriteria = new SuccessCountCriteria();
	
	public void calc(HashMap<String, Object> loanInfoMap) {
		totalPrincipal = Double.parseDouble(loanInfoMap.get("TotalPrincipal").toString());
		owingAmount = Double.parseDouble(loanInfoMap.get("OwingAmount").toString());
		loanAmount = Double.parseDouble(loanInfoMap.get("Amount").toString());
		
		debtTotalRate = totalPrincipal!=0?(owingAmount + loanAmount)/totalPrincipal:1;
		
		certificateValidate = (int)loanInfoMap.get("CertificateValidate");
		
		//educationDegree ���������ơ�ר�ơ�˶ʿ���о�����ר������ר�ƣ���ְ��
		educationDegree = (String)loanInfoMap.get("EducationDegree");
		
		//studyStyle ���������ڡ����Ž��������ˡ���ͨ����ͨȫ���ơ��о���������������Ѳ�����ѧ���ԡ��Կ�
		studyStyle = (String)loanInfoMap.get("StudyStyle");
		
		criteriaCertificate = certificateValidate >0 ;
		
		criteriaBachelor = JsonUtil.decodeUnicode(educationDegree).equals("����") 	&& 
				( JsonUtil.decodeUnicode(studyStyle).equals("��ͨ") 					|| 
				  JsonUtil.decodeUnicode(studyStyle).equals("��ͨȫ����") 	);

		criteriaMaster = ( JsonUtil.decodeUnicode(educationDegree).equals("˶ʿ") 	|| 
						JsonUtil.decodeUnicode(educationDegree).equals("�о���")) 	&& 
						( JsonUtil.decodeUnicode(studyStyle).equals("��ͨ") 			|| 
						JsonUtil.decodeUnicode(studyStyle).equals("��ͨȫ����")  	|| 
						JsonUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) {
		calc(loanInfoMap);
		if(debtTotalRate < 0.25 && criteriaMaster){
			return PERFECT;
		}else if(debtTotalRate < 0.33 && criteriaMaster){
			return GOOD;
		}else if(debtTotalRate < 0.25 && criteriaBachelor){
			return OK;
		}
		return NONE;
	}
	public String getCriteriaName(){
		return "EduDebtPro";
	}
}
