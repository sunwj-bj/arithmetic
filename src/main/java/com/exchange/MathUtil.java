package com.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 网格交易工具类
 */
public class MathUtil {
    /**
     * 计算等差，输入网格创建时的持仓成本，预测的最低价格，网格密度三个参数
     * @return
     */
    public static BigDecimal computeEqualDifference(BigDecimal currentPrice,BigDecimal lowPrice,BigDecimal count){
        return currentPrice.subtract(lowPrice).divide(count);
    }

    /**
     * 获取买卖价格列表
     * @param currentPrice
     * @param lowPrice
     * @param count
     * @return
     */
    public static HashMap<String, List<BigDecimal>> getPriceList(BigDecimal currentPrice, BigDecimal lowPrice, BigDecimal count){
        HashMap returnMap = new HashMap<String, List<BigDecimal>>(2);
        BigDecimal ed = currentPrice.subtract(lowPrice).divide(count);
        List buyList = new ArrayList<BigDecimal>();
        List saleList = new ArrayList<BigDecimal>();
        BigDecimal tempPrice = currentPrice;
        for (int i=1;BigDecimal.valueOf(i).compareTo(count)<=0;i++){
            tempPrice = tempPrice.subtract(ed);
            buyList.add(tempPrice);
        }
        tempPrice = currentPrice;
        for (int i=1;BigDecimal.valueOf(i).compareTo(count)<=0;i++){
            tempPrice = tempPrice.add(ed);
            saleList.add(tempPrice);
        }
        returnMap.put("buyList",buyList);
        returnMap.put("saleList",saleList);
        return returnMap;
    }



    public static void main(String[] args) {
        System.out.println(computeEqualDifference(BigDecimal.valueOf(1.430),BigDecimal.valueOf(1.200),BigDecimal.valueOf(5)));
        System.out.println(getPriceList(BigDecimal.valueOf(1.430),BigDecimal.valueOf(1.200),BigDecimal.valueOf(5)));
    }
}
