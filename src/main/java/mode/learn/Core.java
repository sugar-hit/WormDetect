//package core.learn;
//
//import dao.ElasticSearch;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.graphx.EdgeContext;
//import org.apache.spark.graphx.Graph;
//import util.Time;
//
//import java.util.Map;
//
//public class Core {
//    public void start () {
//        DataFormat df = new DataFormat();
//        JavaPairRDD<String, Iterable<Map<String, Object>>> rdd =
//                df.format(ElasticSearch.retrieve(0L, Time.now())).groupByKey();
//        JavaRDD<String> node ;
//        JavaRDD<String> vertix;
//        Graph<String, Long> graph ;
//        graph.aggregateMessages(
////            new EdgeContext<>()
//                new Msg
//        ).collect();
//    }
//}
