package mode.learn;

import dao.ElasticSearch;
import dao.Spark;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import dao.graph.GraphX;
import org.apache.spark.graphx.Graph;
import util.Time;

import java.util.Map;

public class LearnCore {
    public void execute() {
        Spark spark = new Spark();
        ElasticSearch elasticSearch = new ElasticSearch(spark.context());
        JavaPairRDD<String, Map<String, Object>> esData = elasticSearch.search(0L, Time.now());
        JavaRDD<Map<String, Object>> esDataRDD = esData.values();
//        Spark2 spark2 = new Spark2();
        GraphX graphX = new GraphX();
        Graph<String, String> graph = graphX.graphGenerator(esDataRDD.rdd());
//        graphX.graphGenerator(elasticSearch2.search(spark2.context(), 0L, Time.now()));
        long outDegreeAvg = graphX.graphOutDegreeAVG(graph);
        System.out.println(outDegreeAvg);
    }

}
