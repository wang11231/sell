package com.art.sell.util;

/**
 * @author hao.chang
 */
public class RandomUtil {

    public static  String getPwd(){
        int newNum = (int)((Math.random()*9+1)*100000);
        return String.valueOf(newNum);
    }
}