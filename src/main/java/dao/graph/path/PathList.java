package dao.graph.path;

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
        if (!pathList.contains(stringBuilder.toString()))
            pathList.add(stringBuilder.toString());
    }

    public static ArrayList<String> list() {
        return pathList;
    }

    public static Long pathLengthSub () {
        if (pathList == null)
            return 0L;
        Long result = 0L;
        for (String str: pathList) {
            if (!str.contains("->"))
                continue;
            result += str.split("->").length;
        }

        return result;
    }

    public static Long pathSub () {
        if (pathList == null)
            return 0L;
        return Long.parseLong(Integer.toString(pathList.size()));
    }
}
