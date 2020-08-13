package com.dynamic;


import com.alibaba.fastjson.JSON;

/**
 *
 * 国王和金矿问题
 *
 * 动态规划的三个核心要素：
 * 1、最优子结构
 * 2、边界
 * 3、状态转移公式
 * @author sunwj
 */
public class GetMaxGold {
    /**
     *最优子结构(假设10人，4个金矿)：
     * 10个人4金矿（第五金矿不挖的时候），10减去挖第五金矿的人数要求然后剩下4金矿（第五金矿挖的时候）
     * F（5，10） = Max（F（4，10），F（4，10-P[4]）+ G [4]）
     * ==> F（N，W） = Max（F（N-1，W），F（N-1，W-P[N-1]）+G[N-1]）
     *
     * F(n,w)=0 (n<=1,w<p[0]);
     * F(n,w)=g[0] (n==1,w>=p[0]);
     * F(n,w)=F(n-1,w) (n>1,w<p[n-1]);
     * F(n,w)=Max(F(n-1,w),F(n-1,w-p[n-1])+g[n-1]) (n>1,w>p[n-1])
     *
     *对于一个金矿，要么全部开采，要么不开采
     * @param n 金矿数量
     * @param w 矿工总人数
     * @param g 每个金矿的黄金储量
     * @param p 每个金矿所需矿工数量
     * @return
     */
    public static int getMostGold(int n, int w, int[] g, int[] p){
        //用来临时存储上一行的结果,+1是为了兼容0人
        int[] preResults = new int[w+1];
        //用来保存当前行计算的结果
        int[] results = new int[w+1];

        //没有金矿或者没有工人或者有一个矿但是人数不够，直接返回0
        if (n<=0||w<=0||(w<p[0]&&n==1)){
            return 0;
        }

        //先把第一行初始化了
        for (int i = 0; i <=w; i++) {
            if (i<p[0]){
                preResults[i]=0;
            }else {
                preResults[i]=g[0];
            }
        }
        System.out.println("第1个矿："+ JSON.toJSONString(preResults));

        //外层循环是金矿的数量,内层循环是工人数量
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <=w; j++) {
                if (j<p[i]){
                    results[j]=preResults[j];
                }else {
                    results[j]= Math.max(preResults[j],preResults[j-p[i]]+g[i]);
                }
            }
            //其中需要注意的是，使用浅拷贝（preResults = results）会产生数据问题
            for (int k = 0; k < preResults.length; k++) {
                preResults[k]=results[k];
            }
            System.out.println("第"+(i+1)+"个矿："+ JSON.toJSONString(preResults));
        }
        return results[w];
    }

    public static void main(String[] args) {
        int[] p = new int[]{5,5,3,4,3};
        int[] g = new int[]{400,500,200,300,350};
        int goldAmount = getMostGold(5,10,g,p);
        System.out.println(goldAmount);
    }
}
