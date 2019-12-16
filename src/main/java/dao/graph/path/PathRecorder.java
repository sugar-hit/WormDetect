package dao.graph.path;

import java.io.Serializable;
import java.util.ArrayList;

public class PathRecorder implements Serializable {
    private static ArrayList<String> pathList = new ArrayList<>();
    private static Long pathListCounts = 0L;
    private static Long pathListLengthSub = 0L;


    public static void append(String path) {
        pathList.add(path);
    }

    public static void append(ArrayList<String> list) {
        if (list == null)
            return;
        if (list.size() == 0)
            return;

        pathListCounts++;
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : list) {
            pathListLengthSub++;
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

    public static Long pathCounts () {
        if (pathList == null)
            return 0L;
        return Long.parseLong(Integer.toString(pathList.size()));
    }

    public static Long avgPathLength () {
        return pathListLengthSub / pathListCounts;
    }

    public static void reset () {
        pathList = new ArrayList<>();
    }

    public static void initial () {
        reset();
        pathListLengthSub = 0L;
        pathListCounts = 0L;
    }
}
