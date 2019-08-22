package com.string.kmp;

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
                    //j = next[j],即让pattern模式串指针回溯j - next[j]个单位
                    j = next[j];
                }
           }
           if(j == plen) {
               //遍历完子串则查找成功，返回位置
               return i - j;
           }else {
               //遍历完母串，没有遍历完子串，查找失败
               return -1;
           }
       }

    /**
     * 根据KMP算法，next数组用于存放子串中每一个位子匹配失败后下次滑动到的位置
     * 也就是产生一个部分匹配表,改匹配表记录了当匹配到第j个字符不匹配时需要回溯到的索引位置next[j]
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
                //保存当索引j对应的字符和主串不匹配的时候，就应该回溯到k对应的字符开始比较
                next[j] = k;
            } else {
                //k索引被打回到next[k]位置
                k = next[k];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String source = "ndvgionskiofsunwenjienfoiasndfo";
        String pattern = "sunwenjie";
        int index = kmpStringMatch(source,pattern);
        System.out.println(index);
    }
}
