package dao;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import util.Config;

import java.util.Map;

public class ElasticSearch {

    private static String elasticsearchAddr;
    private static String elasticsearchPort;
//    private static String elasticsearchUsername;
//    private static String elasticsearchPassword;
    private static String elasticsearchIndexAutoCreate = "true";               // Sets true, the index auto generate opened.
    private static String elasticsearchIndexReadMissingAsEmpty = " true";
    private static boolean esparkConfUpdated = false;
    private static JavaSparkContext sc;

    static {
        setElasticSparkConfigs();

    }

    private static void setElasticSparkConfigs() {
        if (esparkConfUpdated)
            return ;
        elasticsearchAddr = Config.getElasticSearchIP();
        elasticsearchPort = Config.getElasticSearchPort();
        Spark.appendSparkConf("es.index.auto.create", elasticsearchIndexAutoCreate);
        Spark.appendSparkConf("es.nodes", elasticsearchAddr);
        Spark.appendSparkConf("es.port", elasticsearchPort);
        Spark.appendSparkConf("es.index.read.missing.as.empty", elasticsearchIndexReadMissingAsEmpty);
        esparkConfUpdated = true;
    }

    private static SparkConf getElasticSparkConfigs() {
        if (!esparkConfUpdated)
            setElasticSparkConfigs();
        return Spark.getSparkConf();
    }

    private static void setSparkContext() {
        if (!esparkConfUpdated) {
            setElasticSparkConfigs();
            sc = Spark.getSparkContext();
            return;
        }

        if (sc == null) {
            sc = Spark.getSparkContext();
        }
    }

    private static JavaSparkContext getSparkContext() {
        if (sc == null) {
            setSparkContext();
        }
        return sc;
    }

    synchronized public static JavaPairRDD<String, Map<String, Object>> retrieve (Long beginTime, Long endTime) {
        if (sc == null)
            setSparkContext();

        String timeRangeSQL =
                "{\"bool\":{\"must\":{\"match\":{\"i_sn_type\":\"TCP\"}},\"filter\":{\"range\":{\"@timestamp\":{\"gte\":"+beginTime+",\"lt\":"+endTime+"}}}}}";
        return JavaEsSpark.esRDD(sc, "au_info_session/au_info_session" , timeRangeSQL);
    }

    synchronized public static JavaPairRDD<String, Map<String, Object>> retrieve (Long beginTime, Long endTime, Long countMoreThan) {
        JavaPairRDD<String, Map<String, Object>> rdd = retrieve(beginTime, endTime);
        if (rdd.count() <= countMoreThan)
            return null;
        return rdd;
    }

}
