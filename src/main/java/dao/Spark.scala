package dao

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.SparkConf

class Spark {

  /**
   *  Spark类
   *  提供了Spark数据处理框架的部分操作封装，
   *  对外提供全局唯一的SparkContext。
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

  // Spark context - single Factory Flag :
  var flag = false

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

  private def configGetter () : SparkConf = {
    sparkConf
  }

  private def contextSetter () : Unit = {
    sparkContext = new JavaSparkContext(configGetter())
    sparkContext.setLogLevel("OFF")
    org.apache.log4j.Logger.getLogger("org").setLevel(org.apache.log4j.Level.OFF)
    org.apache.log4j.Logger.getLogger("org.apache.spark").setLevel(org.apache.log4j.Level.OFF)
    org.apache.log4j.Logger.getLogger("org.apache.spark.SparkConf").setLevel(org.apache.log4j.Level.OFF)
    org.apache.log4j.Logger.getLogger("org.apache.spark.SparkContext").setLevel(org.apache.log4j.Level.OFF)
    org.apache.log4j.Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(org.apache.log4j.Level.OFF)
    java.util.logging.Logger.getLogger("org").setLevel(java.util.logging.Level.OFF)
    java.util.logging.Logger.getLogger("org.apache.spark").setLevel(java.util.logging.Level.OFF)
    java.util.logging.Logger.getLogger("org.apache.spark.SparkConf").setLevel(java.util.logging.Level.OFF)
    java.util.logging.Logger.getLogger("org.apache.spark.SparkContext").setLevel(java.util.logging.Level.OFF)
    java.util.logging.Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(java.util.logging.Level.OFF)
  }

  private def contextGetter () : JavaSparkContext = {
    if (!flag) {
      flag = true
      configSetter()
      contextSetter()
    }
    sparkContext
  }

  def context () : JavaSparkContext = {
    contextGetter()
  }

}
