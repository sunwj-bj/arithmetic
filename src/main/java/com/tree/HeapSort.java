package com.tree;

import java.util.Arrays;

/**堆是一棵顺序存储的完全二叉树。
 * 举例来说，对于n个元素的序列{R0, R1, ... , Rn}当且仅当满足下列关系之一时，称之为堆：
 *
 * (1) R[i] <= R[2i+1] 且 R[i] <= R[2i+2] (小根堆)
 *
 * (2) R[i] >= R[2i+1] 且 R[i] >= [R2i+2] (大根堆)
 *
 * 堆排序解决TtopN问题
 * 我们可以考虑采用小顶堆的思想，在内存中维护一个100大小的小顶堆，
 * 然后每次读取一个数与堆顶进行比较，若比堆顶大，则把堆顶弹出，把当前数据压入堆顶，
 * 然后调整小顶堆，把数据从堆顶下移到一定位置即可，最终得到的小顶堆即为最大的100条数据。
 * （我们可以将整个过程想象成一场挑战赛，战场中有100位英雄，剩下所有人依次挑战，
 * 每次选100人中的最弱的一个挑战，胜了则取代它，最终战场中留下的一定是最强的100个）
 *
 * 算法图解见有道云笔记搜索：堆排序详细图解
 */
public class HeapSort {

    public static void main(String []args){
        int []arr = {9,8,7,6,5,4,3,2,1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void sort(int []arr){
        //1.构建大顶堆
        for(int i=arr.length/2-1;i>=0;i--){
            //从第一个非叶子结点从下至上(arr.length/2-1就是第一个非叶子节点的下标)，从右至左调整结构
            adjustHeap(arr,i,arr.length);
        }
        //2.调整堆结构+交换堆顶元素与末尾元素
        for(int j=arr.length-1;j>0;j--){
            swap(arr,0,j);//将堆顶元素与末尾元素进行交换
            adjustHeap(arr,0,j);//重新对堆进行调整
        }

    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     * @param arr
     * @param i
     * @param length
     */
    public static void adjustHeap(int []arr,int i,int length){
        int temp = arr[i];//先取出当前元素i
        for(int k=i*2+1;k<length;k=k*2+1){//从i结点的左子结点开始，也就是2i+1处开始
            if(k+1<length && arr[k]<arr[k+1]){//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if(arr[k] >temp){//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                arr[i] = arr[k];
                i = k;
            }else{
                break;
            }
        }
        arr[i] = temp;//将temp值放到最终的位置
    }

    /**
     * 交换元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int []arr,int a ,int b){
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
