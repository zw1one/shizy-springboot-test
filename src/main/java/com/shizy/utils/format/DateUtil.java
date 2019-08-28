package com.shizy.utils.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 */
public class DateUtil {

    private static final String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private static int[] simpleWeekDays = {0, 1, 2, 3, 4, 5, 6};

    /************/

    /**
     * 获取当前时间的年，月，日，星期
     *
     * @return
     */
    public static Map<String, Integer> getCurrentTime() {
        Map<String, Integer> dateMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        dateMap.put("year", year);
        dateMap.put("month", month);
        dateMap.put("day", day);
        dateMap.put("weekDay", weekDay - 1);
        return dateMap;
    }

    /**
     * 月份转季度
     */
    public static String month2Quarter(String month) {
        int monthi = Integer.valueOf(month);
        if (monthi >= 1 && monthi <= 3) {
            return "1";
        } else if (monthi >= 4 && monthi <= 6) {
            return "2";

        } else if (monthi >= 7 && monthi <= 9) {
            return "3";

        } else if (monthi >= 10 && monthi <= 12) {
            return "4";
        }
        return null;
    }

    /**
     * 日期转星期
     *
     * @param datetime yyyy-MM-dd
     */
    public static int dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        //return weekDays[w-1];
        return w;
    }

    /**
     * 返回字符串当前时间
     *
     * @return 2019-05-06 11:53:22
     */
    public static String getDate() {
        return formatDate(new Date());
    }

    public static String getDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static long toMillis(String dateStr, String pattern) throws ParseException {
        //"yyyy-MM-dd HH:mm:ss"
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat(pattern).parse(dateStr));
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) throws ParseException {
        dateToWeek("2019-02-25");
        System.out.println();
    }
}
