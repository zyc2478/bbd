package com.autobid.filter;

import com.autobid.util.ConfBean;
import net.sf.json.JSONArray;

public interface ListFilter {
    JSONArray filter(JSONArray ja, ConfBean cb) throws Exception;
}
