package com.interview;

import java.util.ArrayList;

/**
 * 数字范围1-31
 * @author sunwj
 */
public class NumberGame{

    static ArrayList list1 = new ArrayList<Integer>();
    static ArrayList list2 = new ArrayList<Integer>();
    static ArrayList list3 = new ArrayList<Integer>();
    static ArrayList list4 = new ArrayList<Integer>();
    static ArrayList list5 = new ArrayList<Integer>();
    public static void main(String[] args) {
        for (int i=1;i<=31;i++){
            transform_to_binary(i);
            if (check1(i,1)){
                list1.add(i);
            }
            if(check1(i,2)){
                list2.add(i);
            }
            if(check1(i,3)){

                list3.add(i);
            }
            if(check1(i,4)){
                list4.add(i);
            }
            if(check1(i,5)){
                list5.add(i);
            }
        }
            System.out.println(list1);
            System.out.println(list2);
            System.out.println(list3);
            System.out.println(list4);
            System.out.println(list5);
    }

    /**
     * 由于计算机存储都是使用二进制，这种方法就是利用这一点，对原本储存的二进制数与1进行与计算，返回值即为本身。
     * >>>表示无符号右移
     * @param number
     */
    public static void transform_to_binary(int number) {
        for (int i = 4;i >= 0; i--) {
            System.out.print(number >>> i & 1);
        }
        System.out.println("\r");
    }

    /**
     * 判断一个十进制数字的二进制某一位是否为1
     * @param number
     * @param index
     * @return
     */
    public static boolean check1(int number,int index){
        int temp=1<<(index-1);
        if (Integer.valueOf(0).equals(number&temp)){
            return false;
        }else {
            return true;
        }
    }
}
