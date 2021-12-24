package com.geek.learn.thread;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author LiGuanNan
 * @date 2021/12/24 10:46 上午
 */
public class LinkedListTest {
    public static void test() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("D");
        linkedList.add("E");
        linkedList.add("G");
        linkedList.add("A");
        linkedList.add("H");
        System.out.println(linkedList); //[D, E, G, A, H]
        LinkedList<String> linkedList2 = new LinkedList<>(linkedList); //

        String key;
        key = linkedList.getFirst(); // 取表头
        key = linkedList.getLast();  // 取表尾
        key = linkedList.get(2); // 取index = 2
        key = linkedList.peek(); // 取表头
        key = linkedList.peekFirst(); // 取表头
        key = linkedList.peekLast(); // 取表尾
        boolean b = linkedList.remove("G"); // 删除第一个给定元素 删除返回true
        key = linkedList.remove(3); // 删除index = 3 返回删除元素
        key = linkedList.remove();  // 删除表头 返回删除元素
        key = linkedList.removeFirst();  // 删除表头 返回表头
        key = linkedList.removeLast();  // 删除表尾返回表尾
        key = linkedList.poll(); // 删除表头返回 表头
        key = linkedList.pollFirst(); // 删除表头返回 表头
        key = linkedList.pollLast(); // 删除表尾 返回表尾
        linkedList.push("44"); // addFirst
        linkedList.addFirst("3"); // 表头增加元素
        linkedList.addLast("7"); // 表尾增加元素
        b = linkedList.contains("E"); // 包含返回true
        int n = linkedList.size(); // 元素count
        b = linkedList.addAll(linkedList2); // 返回true
        System.out.println(linkedList); // [3, 44, 7, D, E, G, A, H]
        b = linkedList.addAll(3, linkedList2); // 从index = 3 增加
        System.out.println(linkedList);
        //linkedList.clear(); 清空
        key = linkedList.set(4, "EE"); // 设定指定index 的值
        System.out.println(key);
        b = linkedList.offer("3");  // add
        b = linkedList.offerFirst("77"); //addFirst
        b = linkedList.offerLast("22");   // addLast
        b = linkedList.removeFirstOccurrence("E"); // remove("E")
        b = linkedList.removeLastOccurrence("3"); // 删除最后一个

        n = linkedList.indexOf("E"); // 第一次出现的位置index
        n = linkedList.lastIndexOf("G"); // 第后一次出现的位置index

        System.out.println(linkedList); // [77, 3, 44, 7, D, EE, G, A, H, D, G, A, H, 22]
        ListIterator<String> listIterator = linkedList.listIterator(4); // 正向迭代器
        while (listIterator.hasNext()) {
            String string = listIterator.next();
            //System.out.println(string); //D, EE, G, A, H, D, G, A, H, 22]
        }

        Iterator<String> iterator = linkedList.descendingIterator(); // 反向迭代器
        while (iterator.hasNext()) {
            String string = iterator.next();
            //System.out.println(string); //22, H, A, G,D,H,A...
        }

        Object[] objects = linkedList.toArray(); // 转为对象数组
        String[] strings = new String[1];
        strings = linkedList.toArray(strings); // 转为数组
        Object object = linkedList.clone(); // 浅表克隆

        linkedList.add(3, "EE");
        System.out.println(linkedList);
    }

    public static void main(String[] args) {
        test();
    }
}
