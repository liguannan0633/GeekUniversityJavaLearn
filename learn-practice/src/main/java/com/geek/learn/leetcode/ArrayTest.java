package com.geek.learn.leetcode;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LiGuanNan
 * @date 2022/3/22 2:24 下午
 *
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 子数组 是数组中的一个连续部分。
 *

 *
 * 示例 1：
 *
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组[4,-1,2,1] 的和最大，为6 。
 * 示例 2：
 *
 * 输入：nums = [1]
 * 输出：1
 * 示例 3：
 *
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23

 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 *
 */
public class ArrayTest {

    public static void main(String[] args) {
        int i = maxSubArray1(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
        System.out.println(i);
    }

    public static int maxSubArray(int[] nums) {

        //[-2]
        //[-2, 1] = [-2] + [1], [1]
        //[-2, 1] + [-3], [1] + [-3], [-3]
        //[-2, 1, -3, 4],[1, -3, 4],[-3, 4], [4]
        int length = nums.length;

        Map<Integer, List<Integer>> map = Maps.newHashMap();
        int[] des = new int[length];
        des[0] = nums[0];
        for(int i= 1; i<length; i++ ){
            List<Integer> list = new ArrayList<>();
            if(des[i-1] > 0){
                des[i] = des[i-1] + nums[i];
                List<Integer> list1 = map.get(i - 1);
                list.addAll(list1);
                list.add(nums[i]);
                map.put(i,list);
            }else {
                des[i] = nums[i];
                list.add(nums[i]);
                map.put(i,list);
            }
        }

        int max = des[0];
        for(int j = 1; j < length; j++){
            max = Math.max(des[j],max);
        }

        for(int j = 1; j < length; j++){
            if(max == des[j]){
                System.out.println(map.get(j));
            }
        }

        return max;
    }

    public static int maxSubArray1(int[] nums) {

        //[-2]
        //[-2, 1] = [-2] + [1], [1]
        //[-2, 1] + [-3], [1] + [-3], [-3]
        //[-2, 1, -3, 4],[1, -3, 4],[-3, 4], [4]
        int length = nums.length;

        int mid = nums[0];
        int max = nums[0];
        for(int i= 1; i<length; i++ ){
            mid = Math.max(mid + nums[i],nums[i]);
            max = Math.max(mid,max);
        }

        return max;
    }
}
