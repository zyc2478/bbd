package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import com.autobid.util.FormatUtil;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class LoanAmountCriteria implements Criteria, Constants {

    private double loanAmount,highestPrincipal,highestDebt,totalPrincipal;
    private int totalLimitConf, amountBeginConf,amountEndConf;

    public void calc(JSONObject loanInfos, ConfBean cb) {

        loanAmount = Double.parseDouble(loanInfos.get("Amount").toString());
        highestPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("HighestPrincipal"))); //单笔最高借款金额
        highestDebt = Double.parseDouble(loanInfos.get("HighestDebt").toString());                    //历史最高负债
        totalPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("TotalPrincipal")));   //累计借款金额

        //取配置文件
        totalLimitConf = Integer.parseInt(cb.getTotalLimit());              //累计借款最低限额
        amountBeginConf = Integer.parseInt(cb.getAmountBegin());            //本次借款最少金额
        amountEndConf = Integer.parseInt(cb.getAmountEnd());                //本次借款最多金额

    }
    /*
     * 本次借款额度低于1.5W、正常还款5次以上、逾期还清次数（＞30天）最好没有、累计借款金额5K以上、待还金额5K以下、待还金额/历史最高负债越小越好，最好为0等等，
     */
    public int getLevel(JSONObject loanInfos, ConfBean cb) {
        calc(loanInfos, cb);
        if(loanAmount >= amountBeginConf && loanAmount <= amountEndConf && totalPrincipal > totalLimitConf){
            return OK;
        }
        return NONE;
    }

    public String getCriteriaName() {
        return "LoanAmount";
    }
}
