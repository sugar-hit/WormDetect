package dao.graph.path;

import dao.graph.GraphStatistics;
import dao.graph.aggregation.AggregationList;
import utils.FileController;

import java.util.*;

public class PathOutput {
    public void test() {
        ArrayList<String> arr = PathList.list();
//        for (String str : arr) {
//            System.out.println(str);
//        }
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
        long[] outputList = AggregationList.getOutDegreeArray();
        if (array == null)
            return;
        if (array.isEmpty())
            return;
        if (vertexList == null)
            return;
        if (outputList == null)
            return;
        if (outputList.length == 0 || vertexList.length != outputList.length)
            return;
        Map<Long, Long> aggregationMap = new HashMap<>();
        for (int i = 0; i < vertexList.length ; i++) {
            aggregationMap.put(vertexList[i], outputList[i]);
//            System.out.println(vertexList[i] +" / " + outputList[i]);
        }

        Map<String, ArrayList<ArrayList<String>>> pathMap = new HashMap<>();
        for (String str: array) {
            String[] strArray = str.split("->");
            if (strArray.length == 0)
                continue;
            ArrayList<String> pathRecord = new ArrayList<>();
            pathRecord.addAll(Arrays.asList(strArray));
//            pathMap.put(strArray[0], pathRecord);
            pathMapAppend(pathMap, strArray[0], pathRecord);
        }
//        System.out.println(pathMap);
        StringBuilder output = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (String src : pathMap.keySet()) {
            for (ArrayList<String> path : pathMap.get(src)) {
//                System.out.print(src);
//                if (aggregationMap.get(Long.parseLong(src)) == null)
//                    System.out.print("(0)");
//                else
//                    System.out.print("(" + aggregationMap.get(Long.parseLong(src))+ ")");
                sb.setLength(0);
                sb.append(src);
                if (aggregationMap.get(Long.parseLong(src)) == null)
                    sb.append("(0)");
                else
                    sb.append("(").append(aggregationMap.get(Long.parseLong(src))).append(")");
                for (String past: path) {
                    sb.append(past).append("->");
                }
                sb.delete(sb.length() - 2, sb.length());
                // Appending new line tag when many lines records generated.
                // File writer should not append any new line tag after here cause unexpected space line will generate.
                output.append(sb.toString()).append("\r\n");
            }
        }
        FileController.output(output.toString());
        PathList.reset();
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
