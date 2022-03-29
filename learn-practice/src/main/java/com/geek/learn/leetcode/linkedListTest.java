package com.geek.learn.leetcode;

import lombok.Builder;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * @author LiGuanNan
 * @date 2022/3/25 10:06 上午
 */
public class linkedListTest {
    public static void main(String[] args) {
        ListNode node0 = ListNode.builder().val(0).build();
        ListNode node1 = ListNode.builder().val(1).build();
        ListNode node2 = ListNode.builder().val(2).build();

        node0.next = node1;
        node1.next = node2;
        node2.next = null;

        //ListNode test = test(node0, 4);
        //System.out.println(test.val);

        ListNode test = reverseList(node0);
        System.out.println(test.val);
    }

    /**
     * 两个链表,每个节点元素值相加得到一个新的链表
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode r1 = l1;
        ListNode r2 = l2;

        int len1 = 0;
        while (r1 != null){
            r1 = r1.next;
            len1++;
        }
        int len2 = 0;
        while (r2 != null){
            r2 = r2.next;
            len2++;
        }

        int maxLen = Math.max(len1, len2);

        int s = 0;
        ListNode pre = null;
        ListNode ret = null;
        for (int i = 0; i < maxLen; i++){
            int add = s;
            if(l1 != null){
                add += l1.val;
                l1 = l1.next;
            }
            if(l2 != null){
                add += l2.val;
                l2 = l2.next;
            }

            ListNode node = ListNode.builder().val(add % 10).build();
            /*ListNode node = new ListNode(add % 10,null);*/

            s = add/10;

            if(i != 0){
                pre.next = node;
            }else {
                ret = node;
            }

            pre = node;
        }

        if(s != 0){
            pre.next = new ListNode(s,null);
        }
        return ret;
    }


    /**
     * 翻转链表
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        List<ListNode> list = new ArrayList<>();
        while (head != null){
            list.add(head);
            head = head.next;
        }
        int size = list.size();
        ListNode ret = list.get(size - 1);
        for(int i = size -1; i >= 0; i-- ){
            ListNode listNode = list.get(i);
            if(i != 0){
                listNode.next = list.get(i-1);
            }else {
                listNode.next = null;
            }
        }
        return ret;
    }


    /**
     * 旋转链表
     * @param head
     * @param k
     * @return
     */
    public static ListNode xuanzhuanLinkedList(ListNode head, int k) {
        if(head == null || head.next == null || k==0){
            return head;
        }
        int length = 1;
        ListNode tem = head;
        while (tem.next != null){
            length++;
            tem = tem.next;
        }

        if(k % length == 0){
            return head;
        }
        //0 1 2;
        //2 0 1;
        tem.next = head;

        int a = length - k % length;
        while (--a > 0){
            head = head.next;
        }

        ListNode retNode = head.next;
        head.next = null;
        return retNode;

    }

    @Builder
    @ToString
    static class ListNode{
        private int val;
        private ListNode next;
    }
}
