package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

/**
 * @author Richard Zeng
 * @ClassName: BasicCriteria
 * @Description: 最基础的策略，需要通过该策略才可以投标
 * @date 2017年10月13日 下午5:16:24
 */
public class BasicCriteria implements Criteria, Constants {

    private boolean eduBasicCriteria;
    private boolean debtBasicCriteria;
    private boolean beginBasicCriteria;
    private CreditCodeCriteria creditCodeCriteria = new CreditCodeCriteria();
    private DebtRateCriteria debtRateCriteria = new DebtRateCriteria();
    private EducationCriteria educationCriteria = new EducationCriteria();
    private SuccessCountCriteria successCountCriteria = new SuccessCountCriteria();
    private LoanAmountCriteria loanAmountCriteria = new LoanAmountCriteria();
    private OverdueCriteria overdueCriteria = new OverdueCriteria();
    private AgeCriteria ageCriteria = new AgeCriteria();
    private LastSuccessBorrowCriteria lastSuccessBorrowCriteria = new LastSuccessBorrowCriteria();

    public void calc(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {

        int creditCodeLevel = creditCodeCriteria.getLevel(loanInfoMap, cb);
        int debtRateLevel = debtRateCriteria.getLevel(loanInfoMap, cb);
        int educationLevel = educationCriteria.getLevel(loanInfoMap, cb);
        int successCountLevel = successCountCriteria.getLevel(loanInfoMap, cb);
        int loanAmountLevel = loanAmountCriteria.getLevel(loanInfoMap, cb);
        int overdueLevel = overdueCriteria.getLevel(loanInfoMap, cb);
        int ageLevel = ageCriteria.getLevel(loanInfoMap, cb);
        int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfoMap, cb);


        //学历认证优先的策略集合
        eduBasicCriteria = educationLevel > NONE &&
                debtRateLevel > NONE &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //负债策略优先的策略集合
        debtBasicCriteria = debtRateLevel > SOSO &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //信用好的文化菜鸟的策略集合
        beginBasicCriteria = educationLevel > NONE &&
                creditCodeLevel > NONE &&
                successCountLevel > NONE &&
                loanAmountLevel > SOSO &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                lastSuccessBorrowLevel > NONE;

        //System.out.println("eduBasicCriteria:"+eduBasicCriteria);
    }

    public int getLevel(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {
        calc(loanInfoMap, cb);
        //printCriteria(loanInfoMap,cb);
        if (eduBasicCriteria && debtBasicCriteria) {
            //System.out.println("PERFECT");
            return PERFECT;
        } else if (eduBasicCriteria) {
            //System.out.println("GOOD");
            return GOOD;
        } else if (debtBasicCriteria) {
            //System.out.println("OK");
            return OK;
        } else if (beginBasicCriteria) {
            //System.out.println("SOSO");
            return SOSO;
        }
        return NONE;
    }

    public String getCriteriaName() {
        return "Basic";
    }

    public void printCriteria(HashMap<String, Object> loanInfoMap, ConfBean cb) throws Exception {
        int creditCodeLevel = creditCodeCriteria.getLevel(loanInfoMap, cb);
        int debtRateLevel = debtRateCriteria.getLevel(loanInfoMap, cb);
        int educationLevel = educationCriteria.getLevel(loanInfoMap, cb);
        int successCountLevel = successCountCriteria.getLevel(loanInfoMap, cb);
        int loanAmountLevel = loanAmountCriteria.getLevel(loanInfoMap, cb);
        int overdueLevel = overdueCriteria.getLevel(loanInfoMap, cb);
        int ageLevel = ageCriteria.getLevel(loanInfoMap, cb);
        int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfoMap, cb);
        System.out.println("creditCodeLevel:" + creditCodeLevel + ",debtRateLevel:" + debtRateLevel +
                ",educationLevel:" + educationLevel + ",successCountLevel:" + successCountLevel +
                ",loanAmountLevel:" + loanAmountLevel + ",overdueLevel:" + overdueLevel +
                ",ageLevel:" + ageLevel + ",lastSuccessBorrowLevel:" + lastSuccessBorrowLevel);
    }
}
