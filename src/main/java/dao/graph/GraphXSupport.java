package dao.graph;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.graphx.Edge;
import org.apache.spark.graphx.VertexRDD;
import org.apache.spark.rdd.RDD;
import util.Parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphXSupport {
    public void edgeGenerator (JavaRDD<Map<String, Object>> javaRDD, RDD<Edge<String>> edgeRDD) {
        if (javaRDD == null)
            return;
        if (javaRDD.count() == 0)
            return;
//        edge;

        JavaRDD<String> edgeJavaRDD;

    }

    public void vertexGenerator (JavaRDD<Map<String, Object>> javaRDD, RDD<String> vertexRDD) {
        if (javaRDD == null)
            return;
        if (javaRDD.count() == 0)
            return;
        JavaRDD<String> vertexSrc = javaRDD.map(new Function<Map<String, Object>, String>() {
            @Override
            public String call(Map<String, Object> v1) throws Exception {
                return Parser.toStr(v1.get("i_client_ip"));
            }
        });
        JavaRDD<String> vertexDst = javaRDD.map(new Function<Map<String, Object>, String>() {
            @Override
            public String call(Map<String, Object> v1) throws Exception {
                return Parser.toStr(v1.get("i_server_ip"));
            }
        });
        JavaRDD<String> vertexJavaRDD = vertexSrc.union(vertexDst).distinct();
        vertexJavaRDD.collect();
        vertexRDD = vertexJavaRDD.rdd();
    }
}
