package dao.graph.path

import java.util

import dao.graph.GraphX
import dao.graph.aggregation.AggregationList
import org.apache.spark.graphx.{EdgeRDD, Graph, VertexId, VertexRDD}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import utils.parse.ScalaParser

import scala.collection.mutable.ArrayBuffer

class Path extends Serializable {

  def generate (graph: Graph[String, String], sparkSession: SparkSession) : Unit = {
    if (graph == null)
      return
    if (sparkSession == null)
      return
    import sparkSession.implicits._
    val graphX = new GraphX
//    val vertexRDD : VertexRDD[String] = graph.vertices
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
    graphX.getOutDegreeOverMap(graph, 1, sparkSession)
    val vertexArray = AggregationList.srcArray
    generateCore(vertexArray, edgeDF)
  }

  private def generateCore (vertexArrayBuffer : ArrayBuffer[Long], edgeDF: DataFrame): Unit = {
    var i = 0
    val vertexArray = vertexArrayBuffer.toArray
    if (vertexArray.isEmpty)
      return
    vertexArray.map(
      record => {
        i = i + 1
        println("Round " + i + ":  src@" + record)
        generateEachVertex(record.toString, record, "0", edgeDF)
      }
    )
  }

  private def generateEachVertex (prefixPath: String, start: VertexId, timestamp: String, edgeDF: DataFrame) : Long = {
    if (prefixPath == null || timestamp == null || edgeDF == null)
      return start

    val pathList = new util.ArrayList[String]()
    if (!prefixPath.equals(""))
      pathList.add(prefixPath)

    val time : Long = ScalaParser.toLong(timestamp)
    val df : DataFrame = edgeDF.select("src", "dst", "port", "time").where(
      "src = "+ start +
        " and dst !=" + start +
        " and time > " + time
    )

    if (df.count() < 1)
      return start

    val dstArray : util.Iterator[Row] = df.toLocalIterator()
    while (dstArray.hasNext) {
      val s = dstArray.next
//      println(s.getLong(0) + " ->" + s.getLong(1) + ":" + s.getLong(2) + " @" + s.getLong(3) )
      pathList.add(s.getLong(1).toString)
      PathList.append(pathList)
      pathList.remove(pathList.size() - 1)
      if (prefixPath.equals(""))
        generateEachVertex (prefixPath + s.getLong(1), s.getLong(1), s.getLong(3).toString, edgeDF)
      else
        generateEachVertex (prefixPath + "->" + s.getLong(1), s.getLong(1), s.getLong(3).toString, edgeDF)
    }
    start
  }

}
