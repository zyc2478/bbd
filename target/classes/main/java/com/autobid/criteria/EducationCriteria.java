package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.JsonUtil;

public class EducationCriteria implements Criteria,Constants {

	int certificateValidate;
	String educationDegree,studyStyle;
	boolean criteriaCertificate,criteriaBachelor,criteriaMaster;
	//educateValidate = loanInfoObj.getInt("EducateValidate");
	public void calc(HashMap<String, Object> loanInfoMap) {
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
						(JsonUtil.decodeUnicode(studyStyle).equals("��ͨ") 			|| 
						JsonUtil.decodeUnicode(studyStyle).equals("��ͨȫ����")  	|| 
						JsonUtil.decodeUnicode(studyStyle).equals("�Ѳ�"));
				
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) {
		calc(loanInfoMap);
		if(criteriaMaster){
			return PERFECT;
		}else if(criteriaBachelor){
			return GOOD;
		}else if(criteriaCertificate){
			return OK;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "Education";
	}
}
