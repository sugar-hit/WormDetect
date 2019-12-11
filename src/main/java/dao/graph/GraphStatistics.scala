package dao.graph

import dao.graph.path.PathList
import org.apache.spark.graphx.{EdgeRDD, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.util.LongAccumulator
import utils.compare.ScalaCompare
import utils.parse.ScalaParser

class GraphStatistics {
  def getPathLengthSub () : Long = {
    return PathList.pathLengthSub
  }

  def getPathCount () : Long = {
    return PathList.pathSub
//    if (graph == null)
//      return 0
//    if (sparkSession == null)
//      return 0
//    val edge = graph.edges
//    val edgeDF : DataFrame = sparkSession.createDataFrame(edge)
//    edgeDF.sqlContext.sql("SELECT ")
//    0
  }

  def test (graph: Graph[String, String]) : Unit = {
    val rdd = graph.edges
    rdd.map(
      record => println(record.srcId + " -> "+ record.dstId + " : " + ScalaParser.GraphParser.attrTimestampFormat(record.attr) + " @ " + ScalaParser.GraphParser.attrPortFormat(record.attr)  )
    ).collect()
  }

}
