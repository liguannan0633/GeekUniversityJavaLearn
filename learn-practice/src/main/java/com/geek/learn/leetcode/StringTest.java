package com.geek.learn.leetcode;

import java.util.BitSet;

/**
 * @author LiGuanNan
 * @date 2022/3/28 11:29 上午
 */
public class StringTest {

    public static void main(String[] args) {
        //int i = lengthOfLongestSubstring("au");
        //int i = longestCommonSubsequence("oxcpqrsvwf", "shmtulqrypy");
        //String str = longestPalindrome("adasabafasd");
        //String str1 = longestCommonSubString("oxcpqrsvwfasdfdasfasfewfwefqwef", "shmtulqrypyadsfascasdfweqfggdcvsadfasedf");
        String str1 = longestPalindrome("oxcpqrsvwfasdfdasfasfewfwefqwef");
        String str2 = longestPalindrome2("oxcpqrsvwfasdfdasfasfewfwefqwef");
        System.out.println(str1);
        System.out.println(str2);

    }

    /**
     * 动态规划 计算最长回文子串
     * 状态转移方程:
     * P[i,j] = P[i+1,j-1] && s(i+1) == s(j-1)
     * 由上面的推导公式可知,要想求P[i,j],需要先知道P[i+1,j-1],也就是需要先知道s[i+1:j-1]这个子串是不是回文串
     * 所以我们应该先计算长度较小的P[x,y],在计算长度较大的P[x,y]
     * 所以循环应该是按照子串长度从小到大的顺序进行遍历
     */
    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        // 先枚举子串长度
        for (int L = 2; L <= len; L++) {
            // 枚举左边界，左边界的上限设置可以宽松一些
            for (int i = 0; i < len; i++) {
                // 由 L 和 i 可以确定右边界，即 j - i + 1 = L 得
                int j = L + i - 1;
                // 如果右边界越界，就可以退出当前循环
                if (j >= len) {
                    break;
                }

                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 只要 dp[i][L] == true 成立，就表示子串 s[i..L] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }

    /**
     * 归纳法 计算最长回文子串
     */
    public static String longestPalindrome2(String s) {
        if (s.equals("")){
            return "";
        }
        String reverse = new StringBuffer(s).reverse().toString();
        return longestCommonSubString(s, reverse);
    }


    /**
     * 动态规划 计算两个字符串的最长公共子串
     * 状态转移方程
     */
    public static String longestCommonSubString(String text1, String text2) {
        int maxLength = 0;
        int rightPos = 0;
        int m = text1.length(); int n = text2.length();
        int[][] dp = new int[m+1][n+1];
        for(int i = 1; i <= m; i++){
            char c1 = text1.charAt(i-1);
            for(int j = 1; j <= n; j++){
                char c2 = text2.charAt(j-1);
                if(c1 == c2){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else {
                    dp[i][j] =0;
                }
                if(dp[i][j] > maxLength){
                    rightPos = i;
                    maxLength = dp[i][j];
                }
            }
        }
        return text1.substring(rightPos - maxLength, rightPos);
    }

    /**
     * 暴力破解 计算两个字符串的最长公共子串
     * @param s1
     * @param s2
     * @return
     */
    public static String maxSame (String s1, String s2) {
        int start = 0;
        int end = 0;

        for (int i = 0; i < s2.length(); i++) {
            for (int j = i+1; j < s2.length(); j++) {
                if (j - i > end - start && s1.contains(s2.substring(i, j))) {
                    start = s1.indexOf(s2.substring(i, j));
                    end = start+j-i;
                }
            }
        }
        return s1.substring(start,end);
    }


    /**
     * 动态规划 求解两个字符串的最大公共子序列的长度
     * 输入：text1 = "abcde", text2 = "ace"
     * 输出：3
     * 解释：最长公共子序列是 "ace" ，它的长度为 3 。
     *
     * 状态转移方程:
     * if text1[i]=text2[j] then dp[i][j] = dp[i-1][j-1] + 1;
     * if text1[i]!=text2[j] then dp[i][j] = Math.max(dp[i][j-1],dp[i-1][j]);
     * @param text1
     * @param text2
     * @return
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(); int n = text2.length();
        int[][] dp = new int[m+1][n+1];
        for(int i = 1; i <= m; i++){
            char c1 = text1.charAt(i-1);
            for(int j = 1; j <= n; j++){
                char c2 = text2.charAt(j-1);
                if(c1 == c2){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else {
                    dp[i][j] = Math.max(dp[i][j-1],dp[i-1][j]);
                }
            }
        }
        return dp[m][n];
    }


    /**
     * 滑动窗口 求解无重复最大子串
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        int length = s.length();
        if(length <= 1){
            return length;
        }
        BitSet bitSet = new BitSet();
        int max = 0;
        int ri = 0;
        for(int i = 0; i <= length - 1; i++){
            if(i != 0){
                bitSet.clear(s.charAt(i-1));
            }
            while (ri < length){
                if(!bitSet.get(s.charAt(ri))){
                    bitSet.set(s.charAt(ri));
                    ri++;
                }else {
                    break;
                }
                max = Math.max(max,ri - i);
            }
        }
        return max;
    }


}
