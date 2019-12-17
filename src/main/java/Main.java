import dao.ElasticSearch;
import dao.Spark;
import utils.websocket.WebSocket;

public class Main {
    public static void main(String[] args) {
        Spark spark = new Spark();
        ElasticSearch elasticSearch = new ElasticSearch(spark.javaSparkContext());
        WebSocket client = new WebSocket(spark, elasticSearch);
        client.autoReceive();
    }
}
