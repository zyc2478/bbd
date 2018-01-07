package com.autobid.strategy;

import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

public class NoOverdueDebtStrategy implements DebtStrategy {

    private BasicDebtStrategy bds = new BasicDebtStrategy();

    @Override
    public boolean determineStrategy(JSONObject debtInfos, ConfBean cb) {
        //System.out.println("ifCanBuy in NoOverdueDebtStrategy");
        return bds.determineStrategy(debtInfos, cb) && determineNoOverdue(debtInfos);
    }

    private boolean determineNoOverdue(JSONObject debtInfos) {
        boolean noOverdueOk;
        int pastDueNumber = debtInfos.getInt("PastDueNumber");
        int pastDueDay = debtInfos.getInt("PastDueDay");
        noOverdueOk = pastDueNumber == 0 && pastDueDay == 0;
        return noOverdueOk;
    }

}
