package com.example.r2d2.medicalpatient.util;

import org.threeten.bp.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lollipop on 2017/5/1.
 */

public class DateUtil {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date){
        return dateFormat.format(date);
    }

    public static LocalDateTime toLocalDateTime(Date date){;
        return  LocalDateTime.parse(format(date));
    }
}
