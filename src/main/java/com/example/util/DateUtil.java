package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhanghongjian
 * @Date 2019/4/11 17:28
 * @Description
 */
public class DateUtil {

    public static String getTime(String format) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getTime(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getNextYear(String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 1);
        Date date = c.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getNextMonth(String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        Date date = c.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getNextDay(String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        Date date = c.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String changeFormat(String oldFormat, String newFormat, String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        String newTime = "";
        try {
            Date date = dateFormat.parse(data);
            newTime = getTime(newFormat, date);
        } catch (Exception e) {

        }
        return newTime;
    }
}
