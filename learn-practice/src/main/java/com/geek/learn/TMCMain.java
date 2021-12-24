package com.geek.learn;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
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
public class TMCMain {

    @SneakyThrows
    public static void main(String[] args) {

        Disruptor<Element> disruptor = startDisruptor();
        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        /*LocalDateTime time = LocalDateTime.now();
        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();
        for (int i = 0; i < 1000; i++) {
            try {
                LocalDateTime time1 = LocalDateTime.now();
                Duration duration = Duration.between(time, time1);
                if (duration.getSeconds() >= 3) {
                    ringBuffer.publish(sequence);
                    sequence = ringBuffer.next();
                    time = LocalDateTime.now();
                }
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该位置元素的值
                event.set("key:" + new Random().nextInt(20));
            } finally {
                Thread.sleep(100);
            }
        }*/

        List<Long> followerList = Lists.newArrayList();
        /*LocalDateTime offset = LocalDateTime.now();
        for (int i = 0; i < 100; i++) {
            LocalDateTime localDateTime = offset.plusSeconds(new Random().nextInt(3600));
            long time1 = Timestamp.valueOf(localDateTime).getTime();
            followerList.add(time1 / 1000);
        }*/

        followerList = JSONArray.parseArray("[1640314868, 1640314960, 1640314975, 1640315006, 1640315012, 1640315030, 1640315094, 1640315098, 1640315135, 1640315160, 1640315220, 1640315222, 1640315225, 1640315289, 1640315290, 1640315321, 1640315338, 1640315343, 1640315432, 1640315568, 1640315592, 1640315640, 1640315660, 1640315670, 1640315740, 1640315745, 1640315814, 1640315883, 1640315920, 1640315979, 1640316000, 1640316014, 1640316072, 1640316132, 1640316180, 1640316206, 1640316228, 1640316299, 1640316368, 1640316412, 1640316429, 1640316446, 1640316462, 1640316468, 1640316475, 1640316556, 1640316569, 1640316588, 1640316633, 1640316644, 1640316659, 1640316679, 1640316788, 1640316857, 1640316934, 1640316937, 1640316940, 1640317122, 1640317143, 1640317169, 1640317173, 1640317178, 1640317251, 1640317256, 1640317269, 1640317304, 1640317307, 1640317318, 1640317330, 1640317344, 1640317408, 1640317425, 1640317447, 1640317479, 1640317535, 1640317570, 1640317582, 1640317600, 1640317619, 1640317621, 1640317623, 1640317674, 1640317723, 1640317751, 1640317768, 1640317788, 1640317813, 1640317816, 1640317841, 1640317927, 1640317939, 1640318049, 1640318065, 1640318082, 1640318082, 1640318187, 1640318187, 1640318318, 1640318335, 1640318394]",Long.class);

        Collections.sort(followerList);
        System.out.println(" ==followerList== : " + followerList);

        //限制60秒最多5次
        Long time = followerList.get(0);
        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();
        for(Long time1 : followerList){
            long duration = time1 - time;
            if (duration > 60) {
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
        disruptor.shutdown();
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

    private static Disruptor<Element> startDisruptor(){
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
                if(longs.size() >= 5){
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
