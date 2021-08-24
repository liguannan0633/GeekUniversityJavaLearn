package com.geek.learn.thread;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LiGuanNan
 * @date 2021/8/3 11:00 下午
 */
public class LRUByLinkedHashMapTest {

    static class LRUByLinkedHashMap<T,M> extends LinkedHashMap<T,M> {

        /**
         * LRU中最大元素数量
         */
        private int maxSize;

        public LRUByLinkedHashMap(int maxSize) {
            // 容量为最大值/0.75，即最大负载容量为maxSize
            // accessOrder=true  根据查询排序，即最近被使用的放到后面
            //super((int) Math.ceil(maxSize / 0.75) + 1, 0.75f, true);
            super(maxSize, 0.75f, true);
            this.maxSize = maxSize;
        }

        /**
         * 此方法为钩子方法，map插入元素时会调用此方法
         * 此方法返回true则证明删除最早的因子
         * @param eldest
         * @return
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxSize;
        }

    }

    public static void main(String[] args) {
        LRUByLinkedHashMap<Integer,String> lruMap = new LRUByLinkedHashMap<>(10);

        int i = 0;
        while (i < 13){
            i++;
            lruMap.put(i,"hello");
        }

        System.out.println(lruMap);

        lruMap.get(6);
        System.out.println("=====1====" + lruMap);
        lruMap.get(8);
        System.out.println("=====2====" + lruMap);
        lruMap.get(9);
        System.out.println("=====3====" + lruMap);

    }

}
