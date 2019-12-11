package dao.graph

import org.apache.spark.graphx.{EdgeRDD, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.util.LongAccumulator
import utils.compare.ScalaCompare
import utils.parse.ScalaParser

class GraphStatistics {
  def getPathSub (graph: Graph[String, String], sparkSession: SparkSession) : Long = {
    if (graph == null)
      return 0
    if (sparkSession == null)
      return 0
    val edge = graph.edges
    val edgeSrcPairRdd : RDD[(Long, String)] = edge.map(
      record => new Tuple2[Long, String](record.srcId, record.attr)
    )
    val edgeDstPairRdd : RDD[(Long, String)] = edge.map(
      record => (record.dstId, record.attr)
    )
    val edgePathRDD : RDD[(Long, (Option[String], String))] = edgeSrcPairRdd.rightOuterJoin(edgeDstPairRdd)
    val result : LongAccumulator = sparkSession.sparkContext.longAccumulator("PathSub")
    edgePathRDD.map(
      record => {
        if (record._2._1.isEmpty) {
          result.add(1)
        }
        else {
          if (ScalaCompare.timestamp1BiggerThan2(record._2._1.get, record._2._2))
            result.add(1)
        }
      }
    ).collect()
    result.value
  }

  def getPathSum (graph: Graph[String, String], sparkSession: SparkSession) : Long = {
    0
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
