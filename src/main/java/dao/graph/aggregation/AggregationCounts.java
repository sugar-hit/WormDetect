package dao.graph.aggregation;

import java.io.Serializable;

public class AggregationCounts implements Serializable {
    private static int aggregationCounts = 3;
    public static void setAggregationCounts (int value) {
        if (value > 3)
            aggregationCounts = value;
    }
    public static int getAggregationCounts () {
        return aggregationCounts;
    }

}
