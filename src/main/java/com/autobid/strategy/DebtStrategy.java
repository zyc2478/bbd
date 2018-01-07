package com.autobid.strategy;

import com.autobid.util.ConfBean;
import net.sf.json.JSONObject;

public interface DebtStrategy {
    boolean determineStrategy(JSONObject debtInfos, ConfBean cb) throws Exception;
}
