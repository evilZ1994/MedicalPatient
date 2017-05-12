package com.example.r2d2.medicalpatient.util;

import org.threeten.bp.LocalDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lollipop on 2017/5/1.
 */

public class DateUtil {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static DateFormat monthFormat = new SimpleDateFormat("MM");
    private static DateFormat dayFormat = new SimpleDateFormat("dd");
    private static DateFormat hourFormat = new SimpleDateFormat("HH");
    private static DateFormat minuteFormat = new SimpleDateFormat("mm");
    private static DateFormat secondFormat = new SimpleDateFormat("ss");


    public static String format(Date date){
        return dateFormat.format(date);
    }

    public static Date stringToDate(String date){
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getYear(Date date) {
        String year =  yearFormat.format(date);
        return Integer.parseInt(year);
    }

    public static int getMonth(Date date){
        return Integer.parseInt(monthFormat.format(date));
    }

    public static int getDay(Date date){
        return Integer.parseInt(dayFormat.format(date));
    }

    public static int getHour(Date date){
        return Integer.parseInt(hourFormat.format(date));
    }

    public static int getMinute(Date date){
        return Integer.parseInt(minuteFormat.format(date));
    }

    public static int getSecond(Date date){
        return Integer.parseInt(secondFormat.format(date));
    }
}
