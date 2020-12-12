package com.stack;

import java.math.BigDecimal;

/**
 * 计算阶乘
 */
public class Recursion {
    public static void main(String[] args) {
        System.out.println(getRecursion(10));
        System.out.println(getRecursionNew(100));
    }

    /**
     * 递归，递归的方式就不能计算比较大的数了
     * @param n
     * @return
     */
    public static Integer getRecursion(int n){
        if (n==0){
            return 1;
        }
        return n*getRecursion(n-1);
    }

    /**
     * 非递归，注意如果Integer类型去接受最后结果，会返回0
     * @param n
     * @return
     */
    public static BigDecimal getRecursionNew(Integer n){
        BigDecimal result = BigDecimal.ONE;
        if (Integer.valueOf(0).equals(n)){
            return result;
        }
        for (int i=1;i<=n;n--){
            result=result.multiply(BigDecimal.valueOf(n));
        }
        return result;
    }

}
