package dao;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import util.Config;

import java.util.Map;

public class ElasticSearch {
    private JavaSparkContext sc;

    public ElasticSearch(JavaSparkContext sc) {
        this.sc = sc;
    }

    public static String ip () {
        return Config.getElasticSearchIP();
    }

    public static String port () {
        return Config.getElasticSearchPort();
    }

    synchronized public JavaPairRDD<String, Map<String, Object>> search (Long beginTime, Long endTime) {
        String timeRangeSQL =
                "{\"bool\":{\"must\":{\"match\":{\"i_sn_type\":\"TCP\"}},\"filter\":{\"range\":{\"@timestamp\":{\"gte\":"+beginTime+",\"lt\":"+endTime+"}}}}}";
        return JavaEsSpark.esRDD(sc, "au_info_session/au_info_session" , timeRangeSQL);
    }

    synchronized public JavaPairRDD<String, Map<String, Object>> search (Long beginTime, Long endTime, Long countMoreThan) {
        JavaPairRDD<String, Map<String, Object>> rdd = search (beginTime, endTime);
        if (rdd.count() <= countMoreThan)
            return null;
        return rdd;
    }

}
