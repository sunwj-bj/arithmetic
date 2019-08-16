package com.kmp;

public class Kmp {
    /**
     * 串的模式匹配
     * @param source
     * @param pattern
     * @return
     */
    public static int kmpStringMatch(String source, String pattern){
          int i = 0;
          int j = 0;
          char[] s = source.toCharArray();
          char[] p = pattern.toCharArray();
          int slen = s.length;
          int plen = p.length;
          int[] next = getNext(pattern);
           while(i < slen && j < plen){
                   if(j == -1 || s[i] == p[j]){
                        ++i;
                        ++j;
                    }else{
                    //如果j != -1且当前字符匹配失败，则令i不变，
                    //j = next[j],即让pattern模式串右移j - next[j]个单位
                    j = next[j];
                }
               }
           if(j == plen) {
               return i - j;
           }else {
               return -1;
           }
       }

    /**
     * 根据KMP算法，next数组用于存放子串中每一个位子匹配失败后下次滑动到的位置
     * @param ps
     * @return
     */
    public static int[] getNext(String ps) {
        char[] p = ps.toCharArray();
        int[] next = new int[p.length];
        next[0] = -1;
        int j = 0;
        int k = -1;
        //遍历子串字符数组
        while (j < p.length - 1) {
            if (k == -1 || p[j] == p[k]) {
                j++;
                k++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String source = "ndvgionskiofsunwenjienfoiasndfo";
        String pattern = "sunwenjie";
        int index = kmpStringMatch(source,pattern);
        System.out.print(index);
    }
}
