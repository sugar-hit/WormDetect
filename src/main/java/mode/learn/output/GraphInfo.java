package mode.learn.output;

public class GraphInfo {
    public static Long getPathAvgLength() {
        return pathAvgLength;
    }

    public static void setPathAvgLength(Long pathAvgLength) {
        if (pathAvgLength > GraphInfo.pathAvgLength)
            GraphInfo.pathAvgLength = pathAvgLength;
    }

    public static Long getOutdegreeAvg() {
        return outdegreeAvg;
    }

    public static void setOutdegreeAvg(Long outdegreeAvg) {
        if (outdegreeAvg > GraphInfo.outdegreeAvg)
            GraphInfo.outdegreeAvg = outdegreeAvg;
    }

    private static Long pathAvgLength = 3L;
    private static Long outdegreeAvg = 5L;
}
