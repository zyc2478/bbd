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
		
		//educationDegree 包括：本科、专科、硕士、研究生、专升本、专科（高职）
		educationDegree = (String)loanInfoMap.get("EducationDegree");
		
		//studyStyle 包括：函授、开放教育、成人、普通、普通全日制、研究生、网络教育、脱产、自学考试、自考
		studyStyle = (String)loanInfoMap.get("StudyStyle");
		
		criteriaCertificate = certificateValidate >0 ;
		
		criteriaBachelor = JsonUtil.decodeUnicode(educationDegree).equals("本科") 	&& 
				( JsonUtil.decodeUnicode(studyStyle).equals("普通") 					|| 
				  JsonUtil.decodeUnicode(studyStyle).equals("普通全日制") 	);

		criteriaMaster = ( JsonUtil.decodeUnicode(educationDegree).equals("硕士") 	|| 
						JsonUtil.decodeUnicode(educationDegree).equals("研究生")) 	&& 
						(JsonUtil.decodeUnicode(studyStyle).equals("普通") 			|| 
						JsonUtil.decodeUnicode(studyStyle).equals("普通全日制")  	|| 
						JsonUtil.decodeUnicode(studyStyle).equals("脱产"));
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
