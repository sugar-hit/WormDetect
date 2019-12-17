package mode.detect.alert;

import dao.Redis;
import dao.graph.aggregation.AggregationList;
import dao.graph.path.PathRecorder;
import mode.detect.StopListLoader;
import mode.learn.output.GraphInfo;
import utils.parse.JavaParser;
import mode.detect.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PathAlerter {
    public void alert(Redis redis, Long timestart, Long timeend) {
        Alerter alert = new Alerter();
        // 获取学习结果、停用词（操作）结果、获取节点、出度
        ArrayList<String> pathArray = PathRecorder.list();
        ArrayList<String> stopPathArray = StopListLoader.getStopList();
        long[] vertexList = AggregationList.getSrcArray();
        long[] outputList = AggregationList.getOutDegreeArray();
        // 验空
        if (pathArray == null)
            return;
        if (pathArray.isEmpty())
            return;
        if (stopPathArray == null)
            return;
        if (stopPathArray.isEmpty())
            return;
        if (vertexList == null)
            return;
        if (outputList == null)
            return;
        if (outputList.length == 0 || vertexList.length != outputList.length)
            return;
        // 整理节点出度，同步为出度表（map） ，防止后续操作破坏数据完整
        Map<Long, Long> aggregationMap = new HashMap<>();
        for (int i = 0; i < vertexList.length; i++) {
            aggregationMap.put(vertexList[i], outputList[i]);
        }
        //
        Map<String, ArrayList<ArrayList<String>>> pathMap = new HashMap<>();

        for (String str: pathArray) {
            if (stopPathArray.contains(str))
                continue;
            String[] strArray = str.split("->");
            if (strArray.length == 0)
                continue;
            ArrayList<String> pathRecord = new ArrayList<>();
            pathRecord.addAll(Arrays.asList(strArray));
//            pathMap.put(strArray[0], pathRecord);
            pathMapAppend(pathMap, strArray[0], pathRecord);
        }

//        StringBuilder output = new StringBuilder();
        // 用于传递报警信息
        HashMap<String, String> alertMessage = new HashMap<>();

        StringBuilder pathListString = new StringBuilder();
        StringBuilder pathSingle = new StringBuilder();
        for (String src : pathMap.keySet()) {
            for (ArrayList<String> path : pathMap.get(src)) {
                // 检测是否超过平均长度(AL计算得)或者最低长度限制（3）
                if (path.size() < GraphInfo.getPathAvgLength())
                    continue;
                // 由于数据较劣质，测试时候可以关闭这个选项以得到输出，这里关闭了所有长度限制。
                pathSingle.setLength(0);
                for (String past: path) {
                    pathSingle.append(past);
                    if (stopPathArray.contains(pathSingle.toString())) {
                        pathSingle.setLength(0);
                        break;
                    }
                    pathSingle.append("->");
                }
                if (pathSingle.length() == 0)
                    continue;
                pathSingle.delete(pathSingle.length() - 2, pathSingle.length());
                if (pathSingle.length() == 0)
                    continue;
                pathListString.append(alertMessageIpConvert(pathSingle.toString()));
            }
            alertMessage.put("SourceIP", src);
            alertMessage.put("OutDegree", Long.toString(aggregationMap.get(src)));
            alertMessage.put("Routes", pathListString.toString());
            alert.report(redis, alertMessage, timestart, timeend);
        }
        PathRecorder.reset();
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

    private String alertMessageIpConvert (String str) {
        if (str == null)
            return "";
        if (str.equals("") || str.trim().equals(""))
            return "";
        String[] strArray = str.split("->");
        if (strArray.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for(String path : strArray){
            sb.append(JavaParser.Long2IP(Long.parseLong(path))).append("->");
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }
}
