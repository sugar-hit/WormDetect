package dao;

import java.util.ArrayList;

public class PathList {
    private static ArrayList<String> pathList = new ArrayList<>();

    public static void append(String path) {
        pathList.add(path);
    }

    public static void append(ArrayList<String> list) {
        if (list == null)
            return;
        if (list.size() == 0)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : list) {
            stringBuilder.append(path);
            stringBuilder.append("->");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        pathList.add(stringBuilder.toString());
    }

    public static ArrayList<String> list() {
        return pathList;
    }
}
