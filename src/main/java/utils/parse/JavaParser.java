package utils.parse;

public class JavaParser {
    private static String toStr (String string) {
        if (string == null)
            return "";
        return string;
    }

    private static String toStr (int string) {
        return Integer.toString(string);
    }

    private static String toStr (Long string) {
        return Long.toString(string);
    }

    public static String toStr (Object string) {
        if (string == null)
            return "";
        if (string instanceof String)
            return toStr((String) string);
        if (string instanceof Long)
            return toStr((Long) string);
        if (string instanceof Integer)
            return toStr((int) string);
        return "";
    }

    public static String toStr (Object obj1, Object obj2) {
        return toStr(obj1) + toStr(obj2);
    }

    private static Long toLong (String str) {
        if (str == null)
            return 0L;
        return Long.parseLong(str);
    }

    public static Long toLong (Object obj) {
        if (obj == null)
            return 0L;
        if (obj instanceof String)
            return toLong((String) obj);
        if (obj instanceof Long)
            return (Long) obj;
        if (obj instanceof Integer)
            return (Long) obj;
        return 0L;
    }

    public static int toInt (Object obj) {
        if (obj == null)
            return 0;
        if (obj instanceof Integer)
            return (int) obj;
        if (obj instanceof String)
            return toInt((String) obj);
        return 0;
    }

    public static int toInt (String str) {
        if (str == null)
            return 0;
        return Integer.parseInt(str);
    }

    public static String jsonString () {
        return "";
    }

    public static Long ip2Long (String ip4) {
        String[] strList = ip4.trim().split("\\.");
        StringBuffer result = new StringBuffer();
        int tmp = 0;
        for (String str: strList) {
            tmp = Integer.parseInt(str);
            if (tmp < 0) {
                result.append("000");
                continue;
            }
            if (tmp < 10) {
                result.append("00").append(tmp);
                continue;
            }
            if (tmp < 100) {
                result.append("0").append(tmp);
                continue;
            }
            result.append(tmp);
        }
        return Long.parseLong(result.toString());
    }

    public static String Long2IP (Long number) {
        if (number < 1000000000) //1.000.000.000
            return "";
        StringBuilder sb = new StringBuilder(Long.toString(number));
        int length = sb.length();
        String[] ipv4 = new String[4];
        ipv4[3] = sb.substring(length - 3, length);
        ipv4[2] = sb.substring(length - 6, length - 3);
        ipv4[1] = sb.substring(length - 9, length - 6);
        ipv4[0] = sb.substring(0, length - 9);
        sb.setLength(0);
        for (String temp: ipv4) {
            sb.append(temp).append(".");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}
