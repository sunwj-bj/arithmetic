package com.queue;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树的锯齿形层次遍历(Deque实现)
 * 给定一个二叉树，返回其节点值的锯齿形层次遍历。
 * （即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 * 例如：
 * 给定二叉树 [3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回锯齿形层次遍历如下：
 * [
 *   [3],
 *   [20,9],
 *   [15,7]
 * ]
 *
 * Deque是Queue的子接口,我们知道Queue是一种队列形式,而Deque则是双向队列,
 * 它支持从两个端点方向检索和插入元素,因此Deque既可以支持LIFO形式也可以支持FIFO形式.
 * Deque接口是一种比Stack和Vector更为丰富的抽象数据形式,因为它同时实现了以上两者.
 */
public class Solution {

    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> arrayLists = new LinkedList<>();
        if (root==null){
            return arrayLists;
        }

        //flag用来标记入栈顺序(FILO)：0 从左到右；1从右到左
        int flag=1;
        LinkedList layer = new LinkedList<Integer>();
        Deque<TreeNode> deque = new LinkedList<>();
        deque.offerLast(root);
        Deque<TreeNode> nextDeque = new LinkedList<>();
        while (!deque.isEmpty()){
            TreeNode treeNode = deque.pollLast();
            layer.add(treeNode.getVal());
            if (0==flag){
            if (treeNode.getRight()!=null){
                nextDeque.offerLast(treeNode.getRight());
            }
            if (treeNode.getLeft()!=null){
                nextDeque.offerLast(treeNode.getLeft());
            }
            }else if (1==flag){
                if (treeNode.getLeft()!=null){
                    nextDeque.offerLast(treeNode.getLeft());
                }
                if (treeNode.getRight()!=null){
                    nextDeque.offerLast(treeNode.getRight());
                }
            }
            if (deque.isEmpty()){
                Deque<TreeNode> temp = nextDeque;
                nextDeque=deque;
                deque=temp;
                flag=1-flag;
                arrayLists.add(layer);
                layer = new LinkedList<Integer>();
            }

        }
        return arrayLists;

    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        TreeNode left = new TreeNode(9);
        TreeNode right = new TreeNode(20);
        TreeNode left1 = new TreeNode(15);
        TreeNode right1 = new TreeNode(7);
        TreeNode left0 = new TreeNode(98);
        TreeNode right0 = new TreeNode(47);
        root.setLeft(left);
        root.setRight(right);
        right.setLeft(left1);
        right.setRight(right1);
        left.setLeft(left0);
        left.setRight(right0);
        List<List<Integer>> zigzagLevelOrder = zigzagLevelOrder(root);
        System.out.println(zigzagLevelOrder);
    }
}

class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
         TreeNode(int x) { val = x; }
    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
