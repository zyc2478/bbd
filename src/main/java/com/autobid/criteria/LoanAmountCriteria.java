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
        highestPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("HighestPrincipal"))); //������߽����
        highestDebt = Double.parseDouble(loanInfos.get("HighestDebt").toString());                    //��ʷ��߸�ծ
        totalPrincipal = Double.parseDouble(FormatUtil.nullToStr(loanInfos.get("TotalPrincipal")));   //�ۼƽ����

        //ȡ�����ļ�
        totalLimitConf = Integer.parseInt(cb.getTotalLimit());              //�ۼƽ������޶�
        amountBeginConf = Integer.parseInt(cb.getAmountBegin());            //���ν�����ٽ��
        amountEndConf = Integer.parseInt(cb.getAmountEnd());                //���ν�������

    }
    /*
     * ���ν���ȵ���1.5W����������5�����ϡ����ڻ����������30�죩���û�С��ۼƽ����5K���ϡ��������5K���¡��������/��ʷ��߸�ծԽСԽ�ã����Ϊ0�ȵȣ�
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
