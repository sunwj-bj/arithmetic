package com.array;

public class BombMan {
    public static void main(String[] args) {
        int[][] array={{1,0,2,3},
                       {4,6,0,7},
                       {4,8,3,3},
                       {5,0,6,1}};
        //第0列是否变0
        boolean col0=false;
        for (int i=0;i<array.length;i++){
                if (array[i][0]==0){
                    col0=true;
                }
        }

        for (int j=0;j<array.length;j++){
            for (int k=0;k<array[0].length;k++){
                if (array[j][k]==0){
                    array[0][k]=0;
                    array[j][0]=0;
                }
            }
        }
        for (int j=1;j<array[0].length;j++){
            if (array[0][j]==0){
                for (int i=1;i<array.length;i++) {
                    array[i][j] = 0;
                }
            }
        }
        for (int i=array.length-1;i>-1;i--) {
            if (array[i][0]==0){
                for (int j=0;j<array[0].length;j++){
                    array[i][j]=0;
                }
            }
        }
        if (col0){
            for (int i=0;i<array.length;i++){
                array[i][0]=0;
            }
        }
        for (int j=0;j<array.length;j++){
            for (int k=0;k<array[0].length;k++){
                System.out.print(array[j][k]+" ");
            }
            System.out.print("\n");
        }

    }
}
