package com.autobid.dbd;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
//import org.apache.log4j.Logger;

/**
 * @author Richard Zeng
 * @ClassName: DebtDataParser
 * @Description: ծȨ���ݴ������
 * @date 2017��12��22�� ����10:47:12
 */
public class DebtDataParser {

    //private static Logger logger = Logger.getLogger(DebtDataParser.class);

    public static ArrayList<JSONArray> getDebtsCollector(JSONArray debtList) {

        ArrayList<JSONArray> dll = new ArrayList<>();
        int size = debtList.size();
        int partSize = 10;
        int m = size % partSize;
        int partCount;

        if (m > 0) {
            partCount = size / partSize + 1;
        } else {
            partCount = size / partSize;
        }

        for (int i = 0; i < partCount; i++) {
            /*
             * ���1���������35��Ԫ�����һ�֣�partCount = 4,m!=0,i=3ʱ��fromIndexΪ30,toindexΪsize��35
             * ���2���������30��Ԫ�أ�m==0)���һ�֣�m==0,partCount=3,fromIndexΪ20��toindexΪ30
             * ���3���������35��Ԫ�صڶ��֣�i=1,fromIndex=10,toindex=20
             */
            if (m != 0 && i == partCount - 1) {
                List<?> subDebtList = debtList.subList(i * partSize, size);
                //subDebtList = (JSONArray)debtList.subList(i*partSize, size-1);
                //subDebtList = debtList.subList(fromIndex, toIndex)
                JSONArray subDebtArray = new JSONArray();
                subDebtArray.addAll(subDebtList);
                dll.add(subDebtArray);
            } else {
                List<?> subDebtList = debtList.subList(i * partSize, (i + 1) * partSize);
                //subDebtList = (JSONArray)debtList.subList(i*partSize, size-1);
                //subDebtList = debtList.subList(fromIndex, toIndex)
                JSONArray subDebtArray = new JSONArray();
                subDebtArray.addAll(subDebtList);
                dll.add(subDebtArray);
            }
        }
        return dll;
    }

    public static List<Integer> getListingIds(JSONArray dFiltered) {

        List<Integer> listingIds = new ArrayList<>();


        for (int i = 0; i < dFiltered.size(); i++) {
            //System.out.println(dFiltered.getJSONObject(i));
            listingIds.add(dFiltered.getJSONObject(i).getInt("ListingId"));
        }

        return listingIds;
    }

    public static JSONArray parseDebtInfoFromBids(JSONArray dFiltered, JSONArray bidFiltered) {

        JSONArray dbFiltered = new JSONArray();
        for (int i = 0; i < bidFiltered.size(); i++) {
            for (int j = 0; j < dFiltered.size(); j++) {
/*				System.out.println("bidFiltered.getJSONObject(i).getInt(\"ListingId\"):"+bidFiltered.getJSONObject(i).getInt("ListingId"));
				System.out.println("dFiltered.getJSONObject(j).getInt(\"ListingId\"):"+dFiltered.getJSONObject(j).getInt("ListingId"));*/
                if (bidFiltered.getJSONObject(i).getInt("ListingId") == dFiltered.getJSONObject(j).getInt("ListingId")) {
                    dbFiltered.add(dFiltered.getJSONObject(j));
                }
            }
        }

        return dbFiltered;
    }

}
