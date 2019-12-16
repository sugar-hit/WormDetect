import dao.ElasticSearch;
import dao.Spark;
import mode.learn.LearnCore;
import mode.learn.output.GraphInfo;

public class Main {
    public static void main(String[] args) {
        Spark spark = new Spark();
        ElasticSearch elasticSearch = new ElasticSearch(spark.javaSparkContext());
        LearnCore learn = new LearnCore(spark, elasticSearch);
        learn.execute();

    }
}
