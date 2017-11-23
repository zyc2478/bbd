package com.autobid.criteria;

import java.util.HashMap;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.JsonUtil;

public class EducationProCriteria implements Criteria,Constants {

	int certificateValidate;
	String educationDegree,studyStyle;
	boolean criteriaCertificate,criteriaBachelor,criteriaMaster;
	//educateValidate = loanInfoObj.getInt("EducateValidate");
	public void calc(HashMap<String, Object> loanInfoMap) {
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
	}

	public int getLevel(HashMap<String,Object> loanInfoMap) {
		calc(loanInfoMap);
		if(criteriaMaster){
			return PERFECT;
		}else if(criteriaBachelor){
			return GOOD;
		}else{
			return NONE;
		}
	}
	
	public String getCriteriaName(){
		return "EducationPro";
	}
}
