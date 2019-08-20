package com.node;

/**
 * 单链表反转实现，两种方式
 */
public class Reverse {
    /**
     * 递归方法实现单链表反转
     * 比如反转一个链表：0->1->2->3
     * @return
     */
    public static Node reverseByRecursion(Node node){
        if (node==null||node.getNext()==null){
            return node;
        }
        Node recursionNode = reverseByRecursion(node.getNext());
        node.getNext().setNext(node);
        node.setNext(null);
        return recursionNode;
    }

    /**
     * 遍历法反转单链表
     * @param node
     * @return
     */
    public static Node reverseByTraverse(Node node){
        Node pre = null;
        Node next;
        while (node != null) {
            next = node.getNext();
            node.setNext(pre);
            pre = node;
            node = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        Node head = new Node(0,null);
        Node node1 = new Node(1,null);
        Node node2 = new Node(2,null);
        Node node3 = new Node(3,null);
        head.setNext(node1);
        node1.setNext(node2);
        node2.setNext(node3);
        //Node headReturned = reverseByRecursion(head);
        Node headReturned = reverseByTraverse(head);
        Node curNode = headReturned;
        while(curNode!=null){
            if (curNode.getNext()!=null) {
                System.out.print(curNode.getValue()+"->");
            }else{
                System.out.print(curNode.getValue());
            }
            curNode = curNode.getNext();
        }
    }

}
