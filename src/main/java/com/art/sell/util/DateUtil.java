package com.art.sell.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期转换类
 *
 * @author Administrator
 */
public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //上个月
    public static final Integer LAST_MONTH = -1;
    //当前月
    public static final Integer CURRENT_MONTH = 0;
    //下个月
    public static final Integer NEXT_MONTH = 1;
    //year
    public static final String YEAR = "year";
    //month
    public static final String MONTH = "month";
    //day
    public static final String DAY = "day";

    public static Date formatDate(Date date) {
        String format = sdf.format(date);
        try {
            Date parse = sdf.parse(format);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期,格式yyyyMMddHHmmss
     *
     * @return 日期
     */
    public static String getLongDate(String date) throws ParseException {
        Long timestamp = null;
        try {
            timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
            String time = String.valueOf(timestamp);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期,格式yyyyMMddHHmmss
     *
     * @return 日期
     */
    public static String getDate(Date date) throws ParseException {
        Long timestamp = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format1 = format.format(date);
            timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format1).getTime();
            String time = String.valueOf(timestamp);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDatetime(Date datetime) {
        if (datetime == null) {
            return null;
        }
        synchronized (DateUtil.class) {
            return sdf.format(datetime);
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss 类型的字符串转时间
     *
     * @param time
     * @return
     */
    public static Date string2Date(String time) {
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到yyyy-MM-01 00:00:00
     *
     * @param month
     * @return
     */
    public static Date getMonthFirstDay(Integer month) {
        //获取当前时间
        Calendar current = Calendar.getInstance();
        //设置上月
        current.add(Calendar.YEAR, -1);
        current.add(Calendar.MONTH, month);
        //一号
        current.set(Calendar.DAY_OF_MONTH, 1);
        //设置时分秒
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        Date date = current.getTime();
        return formatDate(date);
    }

    public static String getMonthFirstDay() {
        Calendar current = Calendar.getInstance();
        int month = current.get(Calendar.MONTH) + 2;
        return DateFormatUtils.format(getMonthFirstDay(month), PARSE_PATTERNS[0]);
    }

    /**
     * 得到月末yyyy-MM-dd 23:59:59
     *
     * @param month
     * @return
     */
    public static Date getMonthLastDay(Integer month) {
        //获取当前时间
        Calendar current = Calendar.getInstance();
        //设置月份
        current.add(Calendar.MONTH, month + 1);
        //设置最后一天
        current.set(Calendar.DAY_OF_MONTH, 0);
        //设置时分秒
        current.set(Calendar.HOUR_OF_DAY, 23);
        current.set(Calendar.MINUTE, 59);
        current.set(Calendar.SECOND, 59);
        Date date = current.getTime();
        return formatDate(date);
    }

    /**
     * 获取年月 yyyyMMdd
     *
     * @return
     */
    public static String getYearMonthDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取年,月,日
     */
    public static Map<String, Integer> getYearMonthDay(Integer month) {

        Map<String, Integer> map = new HashMap<>();
        //获取当前时间
        Calendar current = Calendar.getInstance();
        //设置月
        current.add(Calendar.MONTH, month + 1);
        //获取年
        map.put(YEAR, current.get(Calendar.YEAR));
        //获取月
        map.put(MONTH, current.get(Calendar.MONTH));
        //获取日
        map.put(DAY, current.get(Calendar.DAY_OF_MONTH));
        return map;
    }

    public static Map<String, Integer> getYearMonthDay() {
        Calendar current = Calendar.getInstance();
        Map<String, Integer> map = new HashMap<>();
        int month = current.get(Calendar.MONTH) + 1;
        int year = current.get(Calendar.YEAR);
        int day = current.get(Calendar.DAY_OF_MONTH);
        map.put(MONTH, month);
        map.put(YEAR, year);
        map.put(DAY, day);
        return map;
    }


    public static final String[] PARSE_PATTERNS = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd", "yyyyMMdd HH:mm:ss", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"};

    /**
     * 解析日期字符串
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(Object dateStr) {
        if (dateStr == null || dateStr.toString().length() == 0) {
            return null;
        }

        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 根据 上个月年、月 获取对应的月份 的 天数
     */
    public static int getPriorMonthDays(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.add(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 根据 当前月月年、月 获取对应的月份 的 天数
     */
    public static int getCurrentMonthDays(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month + 1);
        a.set(Calendar.DATE, 1);
        a.add(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取days天前的开始时间
     *
     * @param days
     * @return
     */
    public static String getStartDayBefore(int days) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return DateFormatUtils.format(calendar.getTime(), PARSE_PATTERNS[0]);
    }
    /**
     * 获取days天前的结束时间
     *
     * @param days
     * @return
     */
    public static String getEndDayBefore(int days) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        return DateFormatUtils.format(calendar.getTime(), PARSE_PATTERNS[0]);
    }

    public static void main(String[] args) {

        System.out.println(getStartDayBefore(3));
        System.out.println(getEndDayBefore(3));
    }
}
