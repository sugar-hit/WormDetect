package dao;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import util.Config;

import java.util.Map;

public class ElasticSearch {
    /**
     * ElasticSearch Class
     * Present many active functions on ES.
     *
     */

    private JavaSparkContext sc;

    /**
     * ElasticSearch(JavaSparkContext sc)
     * Spark Context independence inject
     *
     * @param sc : JavaSparkContext
     */
    public ElasticSearch(JavaSparkContext sc) {
        this.sc = sc;
    }

    /**
     * ip()
     * Static function for get the config file's elastic-search entry IP record.
     *
     * @return ip : String
     */
    public static String ip () {
        return Config.getElasticSearchIP();
    }

    /**
     * port()
     * Static function for get the config file's elastic-search entry port record.
     *
     * @return port: String
     */
    public static String port () {
        return Config.getElasticSearchPort();
    }

    /**
     * search (Long beginTime, Long endTime)
     * Query database and get the data records in javaRDD-like type.
     *
     * @param beginTime : Query's start time [Long]
     * @param endTime : Query's finish time [Long]
     * @return rdd: JavaPairRDD[String, Map[String, Object]]
     */
    synchronized public JavaPairRDD<String, Map<String, Object>> search (Long beginTime, Long endTime) {
        String timeRangeSQL =
                "{\"bool\":{\"must\":{\"match\":{\"i_sn_type\":\"TCP\"}},\"filter\":{\"range\":{\"@timestamp\":{\"gte\":"+beginTime+",\"lt\":"+endTime+"}}}}}";
        return JavaEsSpark.esRDD(sc, "au_info_session/au_info_session" , timeRangeSQL);
    }

    /**
     * search (Long beginTime, Long endTime)
     * Query database and get the data records in javaRDD-like type. Add a least record number limitation.
     * When record less than that means data analysis will not serve a useful result.
     *
     * @param beginTime : Query's start time [Long]
     * @param endTime : Query's finish time [Long]
     * @param countMoreThan : Available record at least. [Long]
     * @return rdd: JavaPairRDD[String, Map[String, Object]]
     */
    synchronized public JavaPairRDD<String, Map<String, Object>> search (Long beginTime, Long endTime, Long countMoreThan) {
        JavaPairRDD<String, Map<String, Object>> rdd = search (beginTime, endTime);
        if (rdd.count() <= countMoreThan)
            return null;
        return rdd;
    }
}
