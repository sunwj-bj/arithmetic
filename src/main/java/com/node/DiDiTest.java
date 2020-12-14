package com.node;

/**
 * 如何判断单链表是否存在环?
 * 环的长度?
 * 环的连接点?
 * 带环链表的长度?
 *
 * 1、对于问题1，使用追赶的方法，设定两个指针slow、fast，从头指针开始，每次分别前进1步、2步。如存在环，则两者相遇；如不存在环，fast遇到NULL退出。
 *
 * 2、对于问题2，记录下问题1的碰撞点p，slow、fast从该点开始，再次碰撞所走过的操作数就是环的长度s。
 *
 * 3、问题3：有定理：碰撞点p到连接点的距离=头指针到连接点的距离，因此，分别从碰撞点、头指针开始走，相遇的那个点就是连接点。
 *
 * 4、问题3中已经求出连接点距离头指针的长度，加上问题2中求出的环的长度，二者之和就是带环单链表的长度
 */
public class DiDiTest {

    public static void main(String[] args) {
        Node node1 = new Node(1, null);
        Node node2 = new Node(2, null);
        Node node3 = new Node(3, null);
        Node node4 = new Node(4, null);
        Node node5 = new Node(5, null);
        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
        node4.setNext(node5);
        node5.setNext(node3);
        System.out.println(isExitsLoop(node1));
        System.out.println(loopLength(node1));
        System.out.println(getJoinNode(node1).getValue());
    }

    //判断链表是否有循环
    public static boolean isExitsLoop(Node head){
        if (head==null||head.getNext()==null){
            return false;
        }
        Node slowNode=head;
        Node fastNode=head;
        while ( fastNode.next!=null) {
            slowNode= slowNode.getNext();
            fastNode= fastNode.getNext().getNext();
            if (slowNode==fastNode){
                return true;
            }
        }
        return false;
    }
    //判断有环的链表环的长度
    public static int loopLength(Node head){
        int length=0;
        boolean countFlag=false;
        if (head==null||head.getNext()==null){
            return length;
        }
        Node slowNode=head;
        Node fastNode=head;
        while ( fastNode.next!=null) {
            slowNode= slowNode.getNext();
            fastNode= fastNode.getNext().getNext();
            if (slowNode==fastNode){
                if (countFlag){
                    return length;
                }
                countFlag=true;
            }
            if (countFlag){
                length++;
            }
        }
        return length;
    }

    /**
     * 返回有闭环的单链表的连接点
     * @param head
     * @return
     */
    public static Node getJoinNode(Node head){
        Node crashNode=null;
        if (head==null||head.getNext()==null){
            return null;
        }
        Node slowNode=head;
        Node fastNode=head;
        while ( fastNode.next!=null) {
            slowNode= slowNode.getNext();
            fastNode= fastNode.getNext().getNext();
            if (slowNode==fastNode){
                crashNode=slowNode;
                break;
            }
        }
        if (crashNode!=null){
            while (crashNode!=head){
                crashNode=crashNode.getNext();
                head=head.getNext();
            }
            return crashNode;
        }
        return null;
    }
}
