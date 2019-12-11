package dao.graph

import java.util

import dao.PathList
import org.apache.spark.graphx.{Edge, EdgeRDD, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import utils.parse.ScalaParser

import scala.collection.LinearSeq

class Path extends Serializable {

  def generate (graph: Graph[String, String], sparkSession: SparkSession) : Unit = {
    if (graph == null)
      return
    if (sparkSession == null)
      return
    import sparkSession.implicits._
    val vertexRDD : VertexRDD[String] = graph.vertices
    val edgeRDD : EdgeRDD[String] = graph.edges
    val edgeDF : DataFrame = edgeRDD.map(
      record => (
        record.srcId,
        record.dstId,
        ScalaParser.toLong(ScalaParser.GraphParser.attrPortFormat(record.attr)),
        ScalaParser.toLong(ScalaParser.GraphParser.attrTimestampFormat(record.attr))
      )
    ).toDF("src", "dst", "port", "time").cache()
    edgeDF.createOrReplaceTempView("edge")
    generateCore(vertexRDD, edgeDF)
  }

  private def generateCore (vertexRDD : VertexRDD[String], edgeDF: DataFrame): Unit = {
    val vertexArray : Array[Long] = vertexRDD.keys.collect()
//    edgeDF.show(20)
    var i = 0
    vertexArray.map(
      record => {
        i = i + 1
        println("Round " + i + ":")
        generateEachVertex(record.toString, record, "0", edgeDF)
      }
    )
  }

  private def generateEachVertex (prefixPath: String, start: VertexId, timestamp: String, edgeDF: DataFrame) : Long = {
    if (prefixPath == null || timestamp == null || edgeDF ==null)
      return start

    val pathList = new util.ArrayList[String]()
    if (!prefixPath.equals(""))
      pathList.add(prefixPath)

    val time : Long = ScalaParser.toLong(timestamp)
    val df : DataFrame = edgeDF.select("src","dst","port","time").where("time > " + time + " and dst !=" + start)

    if (df.count() < 1)
      return start

    val dstArray : util.Iterator[Row] = df.toLocalIterator()
    while (dstArray.hasNext) {
      val s = dstArray.next
//      println(s.getLong(0) + " ->" + s.getLong(1) + ":" + s.getLong(2) + " @" + s.getLong(3) )
      pathList.add(s.getLong(1).toString)
      println(pathList.toString)
      PathList.append(pathList)
      pathList.remove(pathList.size() - 1)
      println(start.toString + "->" + s.getLong(1) + "    @" + s.getLong(3).toString)
      if (prefixPath.equals(""))
        generateEachVertex (prefixPath + s.getLong(1), s.getLong(1), s.getLong(3).toString, edgeDF)
      else
        generateEachVertex (prefixPath + "->" + s.getLong(1), s.getLong(1), s.getLong(3).toString, edgeDF)
    }
    start
  }

}
