package mode.learn;

import dao.ElasticSearch;
import dao.Spark;
import dao.graph.path.Path;
import dao.graph.path.PathRecorder;
import mode.learn.output.GraphInfo;
import org.apache.spark.api.java.JavaRDD;
import dao.graph.GraphX;
import org.apache.spark.graphx.Graph;
import utils.Time;

import java.util.Map;

public class LearnCore {
    private Spark spark;
    private ElasticSearch es;
    private Long startTime = 0L;

    public LearnCore (Spark spark, ElasticSearch es){
        this.spark = spark;
        this.es = es;
        PathRecorder.initial();
    }

    public void execute() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Long start = Time.now();
                System.out.println("Learn mode started at : " + Time.dateTimeFormat(start));
                core();
                System.out.println("Learn mode duration : " + Time.timeFormatEnglish(Time.now()  - start));
                System.out.println("Learning Mode: Out degree avg : " + GraphInfo.getOutdegreeAvg() + "Path Avg length" + GraphInfo.getPathAvgLength());
                Thread.sleep(5 * 60 * 1000);
            }

        } catch (InterruptedException e) {
            System.out.println("Learning mode interrupted successfully." + Time.dateTimeFormat(Time.now()));
        }

    }

    private void core () {
        // Graph drawing tool:
        GraphX graphX = new GraphX();
        // Path generate tool:
        Path path = new Path();
        // Retrieve data
        Long endTime = Time.now();
        JavaRDD<Map<String, Object>> esData = es.search(startTime, endTime).values();
        startTime = endTime;
        // Generate graph (by graph drawing tool)
        Graph<String, String> graph = graphX.graphGenerator(esData.rdd());
        // Statistic on graph by counting paths and each vertex's out degree (Self-terminal dialogs are ignored).
        path.learn(graph, spark.session());
        // And result are saved in the PathRecord/GraphRecord entities.
    }
}
