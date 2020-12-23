package com.sort.quick;

import com.alibaba.fastjson.JSON;

/**
 * @author sunwj
 * 快速排序
 */
public class QuickSort {
    public static void main(String[] args) {
        //int[] array=null;
        //int[] array ={10};
        int[] array={25,66,17,55,30,77,10,8};


        quickSort(array,0,array==null?-1:array.length-1);
        System.out.println(JSON.toJSON(array));
    }

    public static void quickSort(int[] s, int l, int r){
        if (s==null){
            return;
        }
        if (l < r) {
            int i = l, j = r, x = s[l];
            while (i < j){
                while(i < j && s[j] >= x) { // 从右向左找第一个小于x的数
                    j--;
                }
                if(i < j) {
                    s[i++] = s[j];
                }

                while(i < j && s[i] < x) { // 从左向右找第一个大于等于x的数
                    i++;
                }
                if(i < j) {
                    s[j--] = s[i];
                }
            }
            s[i] = x;
            quickSort(s, l, i - 1);
            quickSort(s, i + 1, r);
        }
    }
}
