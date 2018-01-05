package com.autobid.strategy;

//import org.apache.log4j.Logger;

import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

public class OverdueDebtStrategy implements DebtStrategy {

    BasicDebtStrategy bds = new BasicDebtStrategy();
//	private static Logger logger = Logger.getLogger(OverdueDebtStrategy.class); 	

    @Override
    public boolean determineStrategy(JSONObject debtInfos, ConfBean cb) throws Exception {
        //logger.info(debtInfos);
        //printStrategy(debtInfos, cb);
        boolean strategyIsOk = bds.determineStrategy(debtInfos, cb) && determineOverdue(debtInfos, cb);
        return strategyIsOk;
    }

    private boolean determineOverdue(JSONObject debtInfos, ConfBean cb) {
        boolean overdueOk = false;
        int pastDueNumber = debtInfos.getInt("PastDueNumber");
        int pastDueDay = debtInfos.getInt("PastDueDay");
        overdueOk = pastDueNumber <= Integer.parseInt(cb.getDebtOverdueLimit()) && pastDueDay < 15;
        //overdueOk = pastDueDay < 15;
        return overdueOk;
    }
	
/*	private void printStrategy(JSONObject debtInfos,ConfBean cb) throws Exception {
		logger.info("determineBasicStrategy(debtInfos,cb):" + bds.determineStrategy(debtInfos,cb) + 
				", determineOverdue(debtInfos,cb):"+determineOverdue(debtInfos, cb));
	}*/
}
