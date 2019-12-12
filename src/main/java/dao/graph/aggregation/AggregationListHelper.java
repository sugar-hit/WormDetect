package dao.graph.aggregation;

import java.util.ArrayList;
import java.util.HashMap;

public class AggregationListHelper {
    public static int getAggregationAvg (HashMap<Long, Long> map) {
        if (map == null)
            return 0;
        if (map.size() == 0)
            return 0;
        int result = 0;
        for (Long srcIP : map.keySet()) {
            result += map.get(srcIP);
        }
        return result / map.size();
    }

    public static ArrayList<Long> aggregationAvgAboveFilterVertexList (HashMap<Long, Long> map, Long moreThan) {
        if (map == null)
            return null;
        if (map.size() == 0)
            return null;
        if (moreThan <= 0)
            return null;

        ArrayList<Long> result = new ArrayList<>();
        for (Long ipSrc : map.keySet()) {
            if (map.get(ipSrc) > moreThan)
                result.add(ipSrc);
        }
        return result;
    }
}
