package mode.detect;

import dao.ElasticSearch;
import dao.Redis;
import dao.Spark;
import dao.graph.GraphX;
import dao.graph.path.Path;
import dao.graph.path.PathRecorder;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.graphx.Graph;
import utils.Time;

import java.util.Map;

public class DetectCore {
    private Spark spark;
    private ElasticSearch es;
    private Redis redis;
    private Long startTime = 0L;

    public DetectCore(Spark spark, ElasticSearch es, Redis redis) {
        this.spark = spark;
        this.es = es;
        this.redis = redis;
        PathRecorder.initial();
    }

    public void execute () {
        try{
            while (!Thread.currentThread().isInterrupted()) {
                Long start = Time.now();
                System.out.println("Detect mode started at : " + Time.dateTimeFormat(start));
                core();
                System.out.println("Detect mode duration : " + Time.timeFormatEnglish(Time.now()  - start));
                Thread.sleep(5 * 60 * 1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Detect mode interrupted successfully." + Time.dateTimeFormat(Time.now()));

        }
    }

    private void core() {
        // Graph drawing tool:
        GraphX graphX = new GraphX();
        // Path generate tool:
        Path path = new Path();
        // Retrieve data
        Long endTime = Time.now();
        JavaRDD<Map<String, Object>>  esData = es.search(startTime, endTime).values();
        // Load white list operations.
        StopListLoader.setStopList();
        System.out.println(StopListLoader.getStopList());
        // Generate graph (by graph drawing tool)
        Graph<String, String> graph = graphX.graphGenerator(esData.rdd());
        path.detect(graph, spark.session(), redis,startTime, endTime);
        startTime = endTime;
    }
}
