package com.autobid.criteria;

import java.io.IOException;
import java.util.HashMap;

import com.autobid.entity.Criteria;
import com.autobid.entity.Constants;
import com.autobid.util.ConfUtil;
import com.autobid.util.JsonUtil;

public class BeginProCriteria implements Criteria,Constants {

	int certificateValidate;
	String educationDegree,studyStyle;
	boolean criteriaCertificate,criteriaBachelor,criteriaMaster;
	private boolean criteriaA,criteriaC,criteriaD;
	//educateValidate = loanInfoObj.getInt("EducateValidate");
	public void calc(HashMap<String, Object> loanInfoMap) throws NumberFormatException, IOException {
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
		Integer loanAmount = (Integer)loanInfoMap.get("Amount") ;
		Integer highestPrincipal = (Integer)loanInfoMap.get("HighestPrincipal");
		Integer owingAmount = (Integer)loanInfoMap.get("OwingAmount");
		Integer highestDebt = (Integer)loanInfoMap.get("HighestDebt");
		Integer totalPrincipal = (Integer)loanInfoMap.get("TotalPrincipal");
		//System.out.println("loanAmount:"+loanAmount);
		criteriaA = loanAmount >= Integer.parseInt(ConfUtil.getProperty("amount_begin")) && 
				loanAmount <= Integer.parseInt(ConfUtil.getProperty("amount_end")) && 
				totalPrincipal >= Integer.parseInt(ConfUtil.getProperty("total_limit")) &&
				loanAmount < highestPrincipal;
/*		criteriaB = owingAmount + loanAmount < highestDebt && 
				owingAmount < Integer.parseInt(ConfUtil.getProperty("owing_limit")) ;*/
		criteriaC = owingAmount==0 ;
		criteriaD = owingAmount < highestDebt/2 && loanAmount < highestDebt/2;
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) throws NumberFormatException, IOException {
		calc(loanInfoMap);
		if(criteriaMaster && criteriaA && criteriaC && criteriaD){
			return PERFECT;
		}else if(criteriaBachelor && criteriaA && criteriaC && criteriaD) {
			return GOOD;
		}else if(criteriaBachelor || (criteriaA && criteriaC && criteriaD) ){
			return OK;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "BeginPro";
	}
}
