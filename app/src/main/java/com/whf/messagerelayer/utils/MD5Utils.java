package com.whf.messagerelayer.utils;

import java.security.MessageDigest;
import java.util.Calendar;

public class MD5Utils {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 加密
     *
     * @param pwd
     * @return
     */
    public static String hash(String pwd) {

        if (pwd == null) {
            return null;
        }
        try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按分钟加密
     *
     * @param pwd
     * @return
     */
    public static String sign(String pwd) {
        if (pwd == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        // 获得年份
        int year = calendar.get(Calendar.YEAR);
        // 获得月份
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获得日期
        int date = calendar.get(Calendar.DATE);
        // 获得小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 获得分钟
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%04d",year) + String.format("%02d",month) + String.format("%02d",date)+ String.format("%02d",hour) + String.format("%02d",minute) ;
//        System.out.println(time);
        String ret = hash(pwd + time);
//        System.out.println(ret);
        return ret;
    }
}