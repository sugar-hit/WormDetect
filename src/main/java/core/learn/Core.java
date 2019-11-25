package core.learn;

import dao.ElasticSearch;
import org.apache.spark.api.java.JavaPairRDD;
import util.Time;

import java.util.Map;

public class Core {
    public void start () {
        DataFormat df = new DataFormat();
        JavaPairRDD<String, Map<String, Object>> s6x = df.format(ElasticSearch.retrieve(0L, Time.now()));
        System.out.println(s6x.count());
    }
}
