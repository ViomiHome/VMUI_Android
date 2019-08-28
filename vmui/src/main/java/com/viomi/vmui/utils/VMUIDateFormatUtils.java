package com.viomi.vmui.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 说明：日期格式化工具
 * 作者：liuwan1992
 * 添加时间：2018/12/17
 * 修改人：liuwan1992
 * 修改时间：2018/12/18
 */
public class VMUIDateFormatUtils {

    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_MD = "MM-dd";
    private static final String DATE_FORMAT_PATTERN_D = "dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    private static final String DATE_FORMAT_PATTERN_HM = "HH:mm";

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    public static String long2Str(long timestamp, int typ) {
        return long2Str(timestamp, getFormatPattern(typ));
    }

    private static String long2Str(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr 日期字符串
     * @param typ     时间格式
     * @return 时间戳
     */
    public static long str2Long(String dateStr, int typ) {
        return str2Long(dateStr, getFormatPattern(typ));
    }

    private static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getFormatPattern(int typ) {
        if (typ == 0) {
            return DATE_FORMAT_PATTERN_YMD;
        } else if (typ == 1) {
            return DATE_FORMAT_PATTERN_MD;
        } else if (typ == 2) {
            return DATE_FORMAT_PATTERN_D;
        } else if (typ == 3) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_HM;
        }
    }

}
