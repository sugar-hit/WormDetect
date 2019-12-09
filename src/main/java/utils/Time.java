package utils;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class Time implements Serializable {

    /**
     * Time 类
     * 时间类，封装插件内部所需要的全部时间操作，包括：
     * 获取实时时间戳
     */
    private static final String STANDRD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTime dateTime = new DateTime(new Date());

    /**
     * now()
     * 获取实时时间戳，单位精确至毫秒（ms）
     * @return 实施时间戳（ms单位，1970-01-01 00:00:00起，Long类型）
     */
    public static Long now() {
        return dateTime.getMillis();
    }

    /**
     * nowFormat()
     * 获取已格式化的当前时间，
     * 格式为 年-月-日 时（24进制）:分:秒 （示：1970-01-21 17:08:59）
     * @return 已格式化的当前时间 (String类型)
     */
    public static String nowFormat() {
        return dateTime.toString(STANDRD_FORMAT);
    }

    /**
     * timeFormat()
     * 转换时间戳为格式化时间字符串
     * @param timestamp 待转换的时间戳（Long类型）
     * @return 格式化后的当前时间(String类型)
     */
    public static String timeFormat(Long timestamp) {
        return new DateTime(timestamp).toString(STANDRD_FORMAT);
    }
}
