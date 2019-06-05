package com.hrst.induce.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 字符串工具类
 * Created by Lin on 2018/6/21.
 */

public class StrUtil {

    /**
     * 生成随机英文字符串
     * @param length 长度
     * @return
     */
    public static String getEnglishStringRandom(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                builder.append((char)(random.nextInt(26) + temp));
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                builder.append(String.valueOf(random.nextInt(10)));
            }
        }
        return builder.toString();
    }

    /**
     * 生成随机中文字符串
     * @param length 长度
     * @return
     */
    public static String getChineseStringRandom(int length) {
        StringBuilder builder = new StringBuilder();
        int hightPos;
        int lowPos;
        Random random = new Random();
        for(int i = 0; i< length; i++){
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(hightPos)).byteValue();
            b[1] = (Integer.valueOf(lowPos)).byteValue();
            try {
                String str = new String(b, "GBK");
                builder.append(str.charAt(0));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
