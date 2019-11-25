package util;

public class Parser {
    private static String str (String string) {
        if (string == null)
            return "";
        return string;
    }

    private static String str (int string) {
        return Integer.toString(string);
    }

    private static String str (Long string) {
        return Long.toString(string);
    }

    public static String str (Object string) {
        if (string == null)
            return "";
        if (string instanceof String)
            return str((String) string);
        if (string instanceof Long)
            return str((Long) string);
        if (string instanceof Integer)
            return str((int) string);
        return "";
    }

    public static String jsonString () {
        return "";
    }

}
