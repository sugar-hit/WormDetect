package util;

public class Parser {
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
}
