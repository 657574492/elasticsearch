package com.wjj.elasticsearch.test;

/**
 * @author EDZ
 * @date 2020/4/8
 * @description TODO
 */
public class StringTest {

    public static void main(String[] args) {
        String wordString = "我是你王大爷";
        for (int i = 0; i < wordString.length(); ++i) {
            char wordChar = wordString.charAt(i);
            System.out.println(String.valueOf(wordChar));
        }
    }
}
