package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

/**
 * @Author Richard Zeng
 * @ClassName: BasicCriteria
 * @Description: 最基础的策略，需要通过该策略才可以投标
 * @Date 2017年10月13日 下午5:16:24
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
    private GenderCriteria genderCriteria = new GenderCriteria();
    private CertificateCheckCriteria certificateValidateCriteria = new CertificateCheckCriteria();
    private OwingRateCriteria owingRateCriteria = new OwingRateCriteria();

    public void calc(JSONObject loanInfos, ConfBean cb) throws Exception {

        int creditCodeLevel = creditCodeCriteria.getLevel(loanInfos, cb);
        int debtRateLevel = debtRateCriteria.getLevel(loanInfos, cb);
        int educationLevel = educationCriteria.getLevel(loanInfos, cb);
        int successCountLevel = successCountCriteria.getLevel(loanInfos, cb);
        int loanAmountLevel = loanAmountCriteria.getLevel(loanInfos, cb);
        int overdueLevel = overdueCriteria.getLevel(loanInfos, cb);
        int ageLevel = ageCriteria.getLevel(loanInfos, cb);
        int genderLevel = genderCriteria.getLevel(loanInfos,cb);
        int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfos, cb);
        int certifcateValidateLevel = certificateValidateCriteria.getLevel(loanInfos,cb);
        int owingRateLevel = owingRateCriteria.getLevel(loanInfos,cb);


        //学历认证优先的策略集合
        eduBasicCriteria = educationLevel > NONE &&
                debtRateLevel > NONE &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                genderLevel > NONE &&
                certifcateValidateLevel > NONE &&
                owingRateLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //负债策略优先的策略集合
        debtBasicCriteria = debtRateLevel > SOSO &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                certifcateValidateLevel > NONE &&
                genderLevel > NONE &&
                owingRateLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //信用好的文化菜鸟的策略集合
        beginBasicCriteria = educationLevel > NONE &&
                creditCodeLevel > NONE &&
                successCountLevel > NONE &&
                loanAmountLevel > SOSO &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                genderLevel > NONE &&
                certifcateValidateLevel > NONE &&
                owingRateLevel > NONE &&
                lastSuccessBorrowLevel > NONE;

        //System.out.println("eduBasicCriteria:"+eduBasicCriteria);
    }

    public int getLevel(JSONObject loanInfos, ConfBean cb) throws Exception {
        calc(loanInfos, cb);
        //printCriteria(loanInfos,cb);
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

    @SuppressWarnings("unused")
    public void printCriteria(JSONObject loanInfos, ConfBean cb) throws Exception {
        int creditCodeLevel = creditCodeCriteria.getLevel(loanInfos, cb);
        int debtRateLevel = debtRateCriteria.getLevel(loanInfos, cb);
        int educationLevel = educationCriteria.getLevel(loanInfos, cb);
        int successCountLevel = successCountCriteria.getLevel(loanInfos, cb);
        int loanAmountLevel = loanAmountCriteria.getLevel(loanInfos, cb);
        int overdueLevel = overdueCriteria.getLevel(loanInfos, cb);
        int ageLevel = ageCriteria.getLevel(loanInfos, cb);
        int genderLevel = genderCriteria.getLevel(loanInfos,cb);
        int certificateLevel = certificateValidateCriteria.getLevel(loanInfos,cb);
        int owingRateLevel = owingRateCriteria.getLevel(loanInfos,cb);
        int lastSuccessBorrowLevel = lastSuccessBorrowCriteria.getLevel(loanInfos, cb);
        System.out.println("creditCodeLevel:" + creditCodeLevel + ",debtRateLevel:" + debtRateLevel +
                ",educationLevel:" + educationLevel + ",successCountLevel:" + successCountLevel +
                ",loanAmountLevel:" + loanAmountLevel + ",overdueLevel:" + overdueLevel +
                ",ageLevel:" + ageLevel + ",lastSuccessBorrowLevel:" + lastSuccessBorrowLevel +
                ",genderLevel:" + genderLevel + ",certificateLevel:" + certificateLevel +
                ",owingRateLevel: " + owingRateLevel);
    }
}
