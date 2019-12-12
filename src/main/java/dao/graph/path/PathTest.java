package dao.graph.path;

import dao.graph.GraphStatistics;
import dao.graph.aggregation.AggregationList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathTest {
    public void test() {
        ArrayList<String> arr = PathList.list();
        for (String str : arr) {
            System.out.println(str);
        }
        GraphStatistics gs = new GraphStatistics();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Total Length:" + gs.getPathLengthSub());
        System.out.println("Path Counts:" + gs.getPathCount());
        if (gs.getPathCount() == 0) {
            System.out.println("Avg path length: 0");
            return;
        }
        System.out.println("Avg path length: " + gs.getPathLengthSub() / gs.getPathCount());
    }

    public void output () {
        ArrayList<String> array = PathList.list();
        long[] vertexList = AggregationList.getSrcArray();
        if (array == null)
            return;
        if (array.isEmpty())
            return;
        Map<String, ArrayList<ArrayList<String>>> pathMap = new HashMap<>();

        for (String str: array) {
            String[] strArray = str.split("->");
            if (strArray == null)
                continue;
            if (strArray.length == 0)
                continue;
            ArrayList<String> pathRecord = new ArrayList<>();
            for (int i = 0; i < strArray.length ; i++)
                pathRecord.add(strArray[i]);
//            pathMap.put(strArray[0], pathRecord);
            pathMapAppend(pathMap, strArray[0], pathRecord);
        }
        System.out.println(pathMap);
    }

    private void pathMapAppend (Map<String, ArrayList<ArrayList<String>>> pathMap, String key, ArrayList<String> path) {
        if (key == null)
            return;
        if (key.equals(""))
            return;
        if (path == null)
            return;
        if (path.isEmpty())
            return;
        ArrayList<ArrayList<String>> singleSrcPathRecord = new ArrayList<>();
        if (pathMap.get(key) != null)
            singleSrcPathRecord = pathMap.get(key);
        singleSrcPathRecord.add(path);
        pathMap.put(key, singleSrcPathRecord);
    }
}
