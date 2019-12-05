package dao

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

class Spark {

  /**
   *  Spark Class
   *  Provides part of the operation packaging of the Spark data processing framework,
   *  Exposes the globally unique SparkContext to the outside world.
   *
   */
  // Spark Settings :
  private val sparkRunMode : String = "local"
  private val sparkAppName : String = "SparkES"
  private val sparkBroadcastCompress : String = "true"
  private val sparkRDDCompress : String = "true"
  private val sparkIOCompressJarSourcePath : String = "org.apache.spark.io.LZFCompressionCodec"
  private val sparkShuffleFileBuffer : String = "1280k"
  private val sparkReducerMaxSizeInFlight : String = "1024m"
  private val sparkReducerMaxMblnFlight : String = "1024m"

  // Elastic Search requires Settings :
  private val elasticSearchIP : String = ElasticSearch.ip()
  private val elasticSearchPort : String = ElasticSearch.port()
  private val elasticsearchIndexAutoCreate : String = "true"
  private val elasticsearchIndexReadMissingAsEmpty = " true"

  // Private Varieties :
  var sparkConf : SparkConf = _
  var sparkContext : JavaSparkContext = _

  // Initialize Flags :
  var configInitial = false

  /**
   * configSetter()
   * Initialize the relevant settings of Spark Config, including some settings of ElasticSearch-Spark.
   */
  private def configSetter (): Unit = {
    // Initial spark config :
    sparkConf = new SparkConf

    // Spark run configs :
    sparkConf.setMaster(sparkRunMode)
    sparkConf.setAppName(sparkAppName)
    sparkConf.set("spark.rdd.compress", sparkRDDCompress)
    sparkConf.set("spark.broadcast.compress", sparkBroadcastCompress)
    sparkConf.set("spark.io.compression.codec", sparkIOCompressJarSourcePath)
    sparkConf.set("spark.shuffle.file.buffer", sparkShuffleFileBuffer)
    sparkConf.set("spark.reducer.maxSizeInFlight", sparkReducerMaxSizeInFlight)
    sparkConf.set("spark.reducer.maxMblnFlight", sparkReducerMaxMblnFlight)
    // Elastic Search (ESpark) settings :
    sparkConf.set("es.nodes", elasticSearchIP)
    sparkConf.set("es.port", elasticSearchPort)
    sparkConf.set("es.index.auto.create", elasticsearchIndexAutoCreate)
    sparkConf.set("es.index.read.missing.as.empty", elasticsearchIndexReadMissingAsEmpty)
  }

  /**
   * configGetter()
   * Get Spark 's Config
   * @return sparkConf [SparkConf]
   */
  private def configGetter () : SparkConf = {
    if (!configInitial) {
      configSetter()
      configInitial = true
    }
    sparkConf
  }

  /**
   * sessionGetOrCreate()
   * Getting or generate a spark session.
   * @return SparkSession object
   */
  private def sessionGetOrCreate() : SparkSession = {
    SparkSession.builder.config(configGetter()).getOrCreate()
  }

  /**
   * session()
   * Bridge function for sessionGetter()
   * @return SparkSession object
   */
  // sessionGetter()
  def session() : SparkSession = {
    sessionGetOrCreate()
  }

  def context() : SparkContext = {
    session().sparkContext.setLogLevel("OFF")
    session().sparkContext
  }

  def javaSparkContext() : JavaSparkContext = {
    JavaSparkContext.fromSparkContext(context())
  }
}
