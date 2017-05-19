package cn.internetware.support.haixinyuan.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static Date getDate(Long time) {
        if (time == null) {
            return null;
        }
        return new Date(time);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date);
    }

    public static Date parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return formatDate(new Date(timestamp));
    }

    public static void sleepForTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // Ignore it.
        }
    }

    public static Date parseTimestampOfSecond(String nodeTextByXPath) {
        if (nodeTextByXPath == null) {
            return null;
        }
        try {
            return new Date(Long.parseLong(nodeTextByXPath) * 1000);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

