package com.geek.learn;

import com.google.common.collect.Lists;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Builder;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * @author LiGuanNan
 * @date 2021/12/23 3:07 下午
 */
@Builder
public class TMCMain {

    /**
     * 窗口大小,单位秒
     */
    private Integer windowSize;

    /**
     * 阈值
     */
    private Integer threshold;

    @SneakyThrows
    public static void main(String[] args) {
        //模拟测试数据
        List<Long> followerList = Lists.newArrayList();
        LocalDateTime offset = LocalDateTime.now();
        for (int i = 0; i < 100; i++) {
            LocalDateTime localDateTime = offset.plusSeconds(new Random().nextInt(3600));
            long time1 = Timestamp.valueOf(localDateTime).getTime();
            followerList.add(time1 / 1000);
        }
        Collections.sort(followerList);
        System.out.println(" ==followerList== : " + followerList);


        TMCMain tmcMain = TMCMain.builder().windowSize(60).threshold(5).build();
        Disruptor<Element> disruptor = tmcMain.startDisruptor();
        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        tmcMain.check(ringBuffer, followerList);

        disruptor.shutdown();
    }

    private void check(RingBuffer<Element> ringBuffer, List<Long> followerList) {
        //限制60秒最多5次
        Long time = followerList.get(0);
        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();
        for(Long time1 : followerList){
            long duration = time1 - time;
            if (duration > windowSize) {
                ringBuffer.publish(sequence);
                sequence = ringBuffer.next();
                time = time1;
            }
            // 返回可用位置的元素
            Element event = ringBuffer.get(sequence);
            // 设置该位置元素的值
            event.set("key:" + 1,time1);
        }

        ringBuffer.publish(sequence);
    }

    // 队列中的元素
    static class Element {

        private Map<String, List<Long>> map = new ConcurrentHashMap<>(100);

        public Map<String, List<Long>> get() {
            return map;
        }

        public synchronized void set(String key,Long value) {
            if (map.containsKey(key)) {
                map.get(key).add(value);
            } else {
                map.put(key, Lists.newArrayList(value));
            }
        }

        public void clear(){
            map.clear();
        }

    }

    private Disruptor<Element> startDisruptor(){
        // 生产者的线程工厂
        ThreadFactory threadFactory = r -> new Thread(r, "simpleThread");

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = Element::new;

        // 处理Event的handler
        EventHandler<Element> handler = (element, sequence, endOfBatch) -> {
            Map<String, List<Long>> map = element.get();

            //校验是否超限
            for(String key : map.keySet()){
                List<Long> longs = map.get(key);
                if(longs.size() >= threshold){
                    System.out.println("Element: " + map);
                    System.out.printf("=============>key: %s, 次数超限, 总次数%s, list: %s %n", key, longs.size(), longs);
                    System.out.println("最大间隔时长: 秒" + (longs.get(longs.size() - 1) - longs.get(0)));
                }
            }

            element.clear();
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 16;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();

        return disruptor;
    }


}
