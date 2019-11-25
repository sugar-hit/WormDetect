package core.learn;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import scala.Serializable;
import scala.Tuple2;
import util.Parser;

import java.util.Map;

public class DataFormat implements Serializable {
    public JavaPairRDD<String, Map<String, Object>> format(JavaPairRDD<String, Map<String, Object>> rdd) {
        if (rdd == null)
            return null;
        return rdd.values().mapToPair(
                new PairFunction<Map<String, Object>, String, Map<String, Object>>() {
                    StringBuffer key;
                    @Override
                    public Tuple2<String, Map<String, Object>> call(Map<String, Object> map) throws Exception {
                        key = new StringBuffer();
                        key.append(Parser.str(map.get("i_client_ip")));
                        key.append(Parser.str(map.get("i_server_ip")));
                        key.append(Parser.str(map.get("i_server_port")));
                        return new Tuple2<>(key.toString(), map);
                    }
                }
        );
    }


}
