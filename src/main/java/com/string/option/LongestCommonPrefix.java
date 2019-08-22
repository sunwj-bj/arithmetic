package com.string.option;

/**
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 *
 * 如果不存在公共前缀，返回空字符串 ""。
 *
 * 示例 1:
 * 输入: ["flower","flow","flight"]
 * 输出: "fl"
 *
 * 示例 2:
 * 输入: ["dog","racecar","car"]
 * 输出: ""
 *
 *
 * 说明:
 * 所有输入只包含小写字母 a-z 。
 */
public class LongestCommonPrefix {
    public static String getLongestCommonPrefix(String[] strs){
        if(strs.length==0){
            return "";
        }
        if(strs.length==1){
            return strs[0];
        }
        String startStr=strs[0];
        for(int i=startStr.length();i>0;i--){
            String sub = startStr.substring(0,i);
            for(int j=1;j<strs.length;j++){
                if(strs[j].length()<i||!sub.equals(strs[j].substring(0,i))){
                    break;
                }
                if(j==strs.length-1){
                    return sub;
                }
            }

        }
        return "";
    }

    public static void main(String[] args) {
        String[] strs={"flower","flow","flight"};
        System.out.println(getLongestCommonPrefix(strs));
    }
}
