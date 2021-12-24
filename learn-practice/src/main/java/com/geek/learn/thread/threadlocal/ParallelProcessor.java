package com.geek.learn.thread.threadlocal;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author LiGuanNan
 * @date 2021/12/13 4:39 下午
 */
@Data
public class ParallelProcessor<T> {

    public void process(List<T> dataList) {
        // 先校验参数，篇幅限制先省略不写
        dataList.parallelStream().forEach(entry -> {
            doIt();
        });
    }

    private void doIt() {
        String session = ContextHolder.get();
        // do something
        System.out.println("session: " + session);
    }

    public static void main(String[] args) {
        ParallelProcessor<Integer> processor = new ParallelProcessor<>();
        ContextHolder.set("hello");

        List<Integer> list = Lists.newArrayList(1,2,3);
        processor.process(list);

        System.out.println("系统一共有"+Runtime.getRuntime().availableProcessors()+"个cpu(几核)");

        ContextHolder.remove();
    }
}
