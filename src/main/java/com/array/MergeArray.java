package com.array;

/**
 * 给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使得 num1 成为一个有序数组。
 *
 * 说明:
 *
 * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n。
 * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
 * 示例:
 *
 * 输入:
 * nums1 = [1,2,3,0,0,0], m = 3
 * nums2 = [2,5,6],       n = 3
 *
 * 输出: [1,2,2,3,5,6]
 */
public class MergeArray {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i;
        int j;
        int k;
        for(i=0;i<n;i++){
            for(j=0;j<m+i;j++){
                if(nums2[i]<=nums1[j]){
                    for(k=m+i;k>j;k--){
                        nums1[k]=nums1[k-1];
                    }
                    nums1[j]=nums2[i];
                    break;
                }
            }
            //处理nums2数组存在数字大于nums1中所有数的情况
            if(j==m+i){
                nums1[m+i]=nums2[i];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1,2,3,0,0,0};
        int[] b = {2,5,6};
        merge(a,3,b,3);
        for (int i=0;i<a.length;i++){
            System.out.print(a[i]);
        }
    }
}
