package com.example.r2d2.medicalpatient.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by R2D2 on 2017/5/12.
 */

public class CheckChineseUtil {

    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

    public static boolean isContainsChinese(String str)
    {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())    {
            flg = true;
        }
        return flg;
    }
}
