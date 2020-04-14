package com.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TradingStraregy {
    /**
     * 返回净值value增长特定百分比percent后的净值
     * 保留四位小数，四舍五入
     * @param value
     * @param percent
     * @return
     */
    public static BigDecimal computeValue(BigDecimal value,BigDecimal percent){
        return value.multiply(percent).add(value).setScale(4, RoundingMode.HALF_DOWN);
    }
    public static void main(String[] args) {
        String value="1.6280";
        String percent="0.2";
        BigDecimal newValue = computeValue(new BigDecimal(value),new BigDecimal(percent));
        System.out.println(newValue);
    }
}
