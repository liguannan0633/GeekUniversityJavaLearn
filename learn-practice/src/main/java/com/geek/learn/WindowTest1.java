package com.geek.learn;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author LiGuanNan
 * @date 2021/12/22 4:16 下午
 */
@Slf4j
public class WindowTest1 {
    private static Map<Long, List<Long>> check(List<Long> followerList, long windowSize, int threshold) {
        //需要顺序
        log.info(" 条件 windowSize={} ,threshold={}", windowSize, threshold);
        Map<Long, List<Long>> map = new TreeMap<>();
        if (followerList == null || followerList.size() < threshold) {
            return map;
        }
        //检测队列
        LinkedList<Long> checkWindow = new LinkedList<>();
        List<Long> list = new ArrayList<>();
        for (Long followerTime : followerList) {
            checkWindow.offer(followerTime);
            Long first = checkWindow.peek();
            Long diff = followerTime - first;
            if (diff <= windowSize) {
                list.add(followerTime);
            } else {
                checkWindow.removeAll(list);
                list = Lists.newArrayList();
                list.add(followerTime);
                /*List release=Lists.newArrayList();
                if (!checkWindow.isEmpty()) {
                    checkWindow.poll();
                    while (!checkWindow.isEmpty()) {
                        first = checkWindow.peek();
                        diff = followerTime - first;
                        if (diff > windowSize) {
                            release.add(checkWindow.poll());
                        }else{
                            break;
                        }
                    }
                    System.out.println("=================="+release);
                    list.clear();
                    list.addAll(checkWindow);
                }*/
            }
            if (list.size() >= threshold) {
                List<Long> longs = map.get(list.get(0));
                if(CollectionUtils.isNotEmpty(longs)){
                    longs.add(followerTime);
                }else {
                    map.put(list.get(0), list);
                }

                //checkWindow.removeAll(list);
                //list = Lists.newArrayList();
            }
        }
        return map;
    }


    public static void main(String[] args) {

        List<Long> followerList = Lists.newArrayList();
        /*LocalDateTime offset = LocalDateTime.now();
        for (int i = 0; i < 100; i++) {
            LocalDateTime localDateTime = offset.plusSeconds(new Random().nextInt(3600));
            long time1 = Timestamp.valueOf(localDateTime).getTime();
            followerList.add(time1 / 1000);
        }*/

        followerList = JSONArray.parseArray("[1640314868, 1640314960, 1640314975, 1640315006, 1640315012, 1640315030, 1640315094, 1640315098, 1640315135, 1640315160, 1640315220, 1640315222, 1640315225, 1640315289, 1640315290, 1640315321, 1640315338, 1640315343, 1640315432, 1640315568, 1640315592, 1640315640, 1640315660, 1640315670, 1640315740, 1640315745, 1640315814, 1640315883, 1640315920, 1640315979, 1640316000, 1640316014, 1640316072, 1640316132, 1640316180, 1640316206, 1640316228, 1640316299, 1640316368, 1640316412, 1640316429, 1640316446, 1640316462, 1640316468, 1640316475, 1640316556, 1640316569, 1640316588, 1640316633, 1640316644, 1640316659, 1640316679, 1640316788, 1640316857, 1640316934, 1640316937, 1640316940, 1640317122, 1640317143, 1640317169, 1640317173, 1640317178, 1640317251, 1640317256, 1640317269, 1640317304, 1640317307, 1640317318, 1640317330, 1640317344, 1640317408, 1640317425, 1640317447, 1640317479, 1640317535, 1640317570, 1640317582, 1640317600, 1640317619, 1640317621, 1640317623, 1640317674, 1640317723, 1640317751, 1640317768, 1640317788, 1640317813, 1640317816, 1640317841, 1640317927, 1640317939, 1640318049, 1640318065, 1640318082, 1640318082, 1640318187, 1640318187, 1640318318, 1640318335, 1640318394]",Long.class);

        Collections.sort(followerList);

        System.out.println("=====followerList=====" + followerList);

        Map<Long, List<Long>> check = check(followerList, 60, 5);

//        System.out.println(check);

        if (MapUtils.isNotEmpty(check)) {
            for(Long key : check.keySet()){
                List<Long> longs = check.get(key);
                System.out.println("长度: " + longs.size());
                System.out.println("最大间隔时长: 秒" + (longs.get(longs.size() - 1) - longs.get(0)));
            }
            System.out.println(check);
        }

    }



}
