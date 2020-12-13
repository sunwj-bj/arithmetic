package com.array;

/**
 * 搜索旋转数组。给定一个排序后的数组，包含n个整数，但这个数组已被旋转过很多次了，次数不详。
 * 请编写代码找出数组中的某个元素，假设数组元素原先是按升序排列的。若有多个相同元素，返回索引值最小的一个。
 *
 * 示例1:
 *
 *  输入: arr = [15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14], target = 5
 *  输出: 8（元素5在该数组中的索引）
 * 示例2:
 *
 *  输入：arr = [15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14], target = 11
 *  输出：-1 （没有找到）
 *
 */
public class FindNumberInArray {
    public static void main(String[] args) {
        int[] arr={15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14};
        int index = getIndex(arr,1);
        System.out.println(index);
    }

    public static int getIndex(int[] arrs,int target){
        if(arrs.length==0){
            return -1;
        }
        int tempIndex=0;
        int tempint=arrs[0];

        for(int i=1;i<arrs.length;i++){
            if(arrs[i]<tempint){
                tempIndex=i;
            }
            tempint=arrs[i];
        }
        int index1 = getIndex(arrs, 0,tempIndex - 1, target);
        int index2 = getIndex(arrs, tempIndex, arrs.length-1, target);
        if (index1==-1&&index2!=-1){
            return index2;
        }
        if (index2==-1&&index1!=-1){
            return index1;
        }
        if (index1!=-1&&index2!=-1){
            return index1<index2?index1:index2;
        }
        return -1;
    }

    /**
     * 二分法，如果有相同数字，返回最小下标
     * @return
     */
    public static int getIndex(int[] arrs,int start,int end,int target){
        int finalIndex=-1;
        if (arrs.length==1){
            return -1;
        }
        while (start<=end) {
            int mid=(start+end)/2;
            if (target < arrs[mid]) {
                end = mid-1;
            } else if (target > arrs[mid]) {
                start = mid+1;
            }else {
                finalIndex = mid;
                end=mid-1;
            }
        }
        return finalIndex;
    }

}
