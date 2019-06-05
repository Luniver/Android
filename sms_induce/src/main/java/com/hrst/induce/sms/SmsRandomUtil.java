package com.hrst.induce.sms;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

/**
 * 获取随机短信内容
 * Created by Lin on 2018/6/5.
 */

public class SmsRandomUtil {
    /**
     * 获取随机内容
     * @return
     */
    public static String getSmsContent () {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        //去掉"-"符号
        String temp = str.substring(0, 8);

        return temp;
    }

    /**
     * 生成随机数字和字母
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成随机中文字符
     * @param length
     * @return
     */
    public static String getRandomChar(int length) {
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
