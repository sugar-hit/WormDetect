package mode.learn;

import dao.ElasticSearch;
import dao.Spark;
import dao.graph.path.Path;
import dao.graph.path.PathOutput;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import dao.graph.GraphX;
import org.apache.spark.graphx.Graph;
import utils.Time;

import java.util.Map;

public class LearnCore {
    public void execute() {
        Long start = Time.now();
        Spark spark = new Spark();
        ElasticSearch elasticSearch = new ElasticSearch(spark.javaSparkContext());
        JavaPairRDD<String, Map<String, Object>> esData = elasticSearch.search(0L, Time.now());
        JavaRDD<Map<String, Object>> esDataRDD = esData.values();
        GraphX graphX = new GraphX();
        Graph<String, String> graph = graphX.graphGenerator(esDataRDD.rdd());


//        long outDegreeAvg = graphX.graphOutDegreeAvg(graph);
//        System.out.println(outDegreeAvg);
//        System.out.println(inDegreeAvg);
//        graphX.test(graph);
//        graphX.
//        HashMap<Object, Object> hashMap1 = new HashMap<>();
//        hashMap1 = graphX.getOutDegreeOverList(graph, 5, spark.session());
//        if (hashMap1.size() == 0)
//            System.out.println("ERROR ERROR");
//        HashMap<Long, Long> hashMap = new HashMap<>();
//        for (Object recordKey : hashMap1.keySet()) {
//            hashMap.put((Long) recordKey, (Long) hashMap1.get(recordKey));
//            System.out.println((Long)recordKey  + " - " +hashMap.get((Long) recordKey));
//        }

//        GraphStatistics gs = new GraphStatistics();
//        gs.test(graph);
//        System.out.println(gs.getPathSub(graph, spark.session()));
//        System.out.println(Time.now());
//        System.out.println(Time.now().toString().length());
//        System.out.println(ScalaCompare.timestampMin(ScalaCompare.test()));
//        System.out.println(ScalaCompare.timestampMin(ScalaCompare.test(), "104"));
//        Long start = Time.now();

//        System.out.println(Time.timeFormatEnglish(start));


//
        System.out.println(Time.dateTimeFormat(start));
        Path path = new Path();
        path.generate(graph, spark.session());
//        PathOutput pathOutput = new PathOutput();
        System.out.println("___________________________________________");
//        pathOutput.output();
        System.out.println(Time.timeFormatEnglish(Time.now()  - start));
    }
}
