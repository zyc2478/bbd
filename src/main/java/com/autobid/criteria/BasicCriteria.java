package com.autobid.criteria;

import com.autobid.entity.Constants;
import com.autobid.entity.Criteria;
import com.autobid.util.ConfBean;

import java.util.HashMap;

/**
 * @author Richard Zeng
 * @ClassName: BasicCriteria
 * @Description: ������Ĳ��ԣ���Ҫͨ���ò��Բſ���Ͷ��
 * @date 2017��10��13�� ����5:16:24
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


        //ѧ����֤���ȵĲ��Լ���
        eduBasicCriteria = educationLevel > NONE &&
                debtRateLevel > NONE &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //��ծ�������ȵĲ��Լ���
        debtBasicCriteria = debtRateLevel > SOSO &&
                creditCodeLevel > NONE &&
                successCountLevel > SOSO &&
                loanAmountLevel > NONE &&
                overdueLevel > NONE &&
                ageLevel > NONE &&
                lastSuccessBorrowLevel > NONE;
        //���úõ��Ļ�����Ĳ��Լ���
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
