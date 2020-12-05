package com.array;

import java.util.Arrays;

/**
 * 合并两个有序数组到新数组
 */
public class MergeArray2New {
    public static void main(String[] args) {
        int[] arr1={2,6,8,10,22,36,350};
        int[] arr2={6,6,9,11,23,100,120,300};
        int[] arr= new int[arr1.length+arr2.length];

        merge(arr1, arr2, arr);
        System.out.println(Arrays.toString(arr));
    }

    private static void merge(int[] arr1, int[] arr2, int[] arr) {
        int i=0;
        int j=0;
        int k=0;
        while ((i+j)<(arr1.length+arr2.length)){
            if (i==arr1.length){
                while (j<arr2.length){
                    arr[k++]=arr2[j++];
                }
                //这里直接return，一方面提高效率，另一方面防止下面判断空指针
                return;
            }
            if (j==arr2.length){
                while (i<arr1.length){
                    arr[k++]=arr1[i++];
                }
                //这里直接return，一方面提高效率，另一方面防止下面判断空指针
                return;
            }
            if (arr1[i]<arr2[j]){
                arr[k++]=arr1[i++];
            }else {
                arr[k++]=arr2[j++];
            }
        }
    }
}
