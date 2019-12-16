package mode.learn.output;

import utils.Config;
import utils.FileController;

public class GraphOutput {
    public static void update(Long avgPathLength, Long avgOutDegree) {
        GraphInfo.setPathAvgLength(avgPathLength);
        GraphInfo.setOutdegreeAvg(avgOutDegree);
//        StringBuffer sb = new StringBuffer();
//        sb.append("avgPathLength=").append(avgPathLength).append("\r\n");
//        sb.append("avgOutDegree=").append(avgOutDegree).append("\r\n");
//        FileController.output(Config.getLearningModeSavingPath() + "topologic_record.iedb", sb.toString());
    }
}
