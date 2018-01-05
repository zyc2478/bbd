package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.ConfUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LastSuccessBorrowCriteria implements Criteria, Constants {

    boolean criteriaMShort, criteriaMLong, criteriaFShort, criteriaFLong;
    private int successCount;

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {
        long diffDay = 0;
        successCount = (int) loanInfoMap.get("SuccessCount");
        if (successCount >= 1) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date lastDate = df.parse((String) loanInfoMap.get("LastSuccessBorrowTime"));
            Date loanDate = df.parse(df.format(new Date()));
            long diffTime = loanDate.getTime() - lastDate.getTime();
            diffDay = diffTime / (1000 * 60 * 60 * 24);
        }

        criteriaMShort = diffDay > Integer.parseInt(ConfUtil.getProperty("short_mdays"));
        criteriaMLong = diffDay > Integer.parseInt(ConfUtil.getProperty("long_mdays"));
        criteriaFShort = diffDay > Integer.parseInt(ConfUtil.getProperty("short_fdays"));
        criteriaFLong = diffDay > Integer.parseInt(ConfUtil.getProperty("long_fdays"));
    }


    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {
        calc(loanInfoMap, cb);
        if (criteriaMLong || criteriaFLong) {
            return GOOD;
        } else if (criteriaMShort || criteriaFShort) {
            return OK;
        } else {
            return NONE;
        }
    }

    public String getCriteriaName() {
        return "LastSuccessBorrow";
    }
}

