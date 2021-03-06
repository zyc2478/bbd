package com.autobid.strategy;

import com.autobid.criteria.CreditCodeCriteria;
import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

public class BasicDebtStrategy implements DebtStrategy {

    //private static Logger logger = Logger.getLogger(BasicDebtStrategy.class);

    @Override
    public boolean determineStrategy(JSONObject debtInfos, ConfBean cb) {

        //printStrategy(debtInfos, cb);
        return determineOwingNumber(debtInfos) &&
                determineStatusId(debtInfos) &&
                determinePreferenceDegree(debtInfos, cb) &&
                determineCreditCode(debtInfos, cb);
    }

    private boolean determineOwingNumber(JSONObject debtInfos) {
        int lm = debtInfos.getInt("ListingMonths");
        int on = debtInfos.getInt("OwingNumber");
        boolean result = false;
        if (lm == 6 && on >= 3 && on <= 4) result = true;
        if (lm == 12 && on >= 4 && on <= 9) result = true;
        if (lm == 9 && on >= 3 && on <= 6) result = true;
        if (lm == 24 && on >= 5 && on <= 12) result = true;
        return result;
    }

    private boolean determineStatusId(JSONObject debtInfos) {
        int status = debtInfos.getInt("StatusId");
        return status == 1;
    }

    private boolean determinePreferenceDegree(JSONObject debtInfos, ConfBean cb) {
        double pd = debtInfos.getDouble("PreferenceDegree");
        //优惠度越小越优惠
        return pd <= Double.parseDouble(cb.getDebtPreferLimit());
    }

    private boolean determineCreditCode(JSONObject debtInfos, ConfBean cb) {
        int currentCredit,creditLimit;
        CreditCodeCriteria ccc = new CreditCodeCriteria();
        String creditCodeLimit = cb.getCreditLimit();
        String currentCode = debtInfos.getString("CurrentCreditCode");
        currentCredit = ccc.switchCredit(currentCode);
        creditLimit = ccc.switchCredit(creditCodeLimit);
        return currentCredit >= creditLimit;
    }

/*	private void printStrategy(JSONObject debtInfos,ConfBean cb) throws IOException {
		logger.info("determineOwingNumber(debtInfos):"+determineOwingNumber(debtInfos)+", determineStatusId(debtInfos):"+
				determineStatusId(debtInfos)+", determinePreferenceDegree(debtInfos):"+determinePreferenceDegree(debtInfos,cb)+
				", determineCreditCode(debtInfos,cb)):"+ determineCreditCode(debtInfos,cb));
	}*/
}
