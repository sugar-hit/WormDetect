package dao.graph

import util.Parser
import java.util

import org.apache.spark.graphx.{Edge, EdgeRDD, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class GraphX extends Serializable {
  def graphGenerator (rdd : RDD[java.util.Map[String, Object]]) : Graph[String, String] = {
    val vertexSrc: RDD[String] = rdd.map(
      record => Parser.toStr(record.get("i_client_ip"))
    )
    val vertexDst: RDD[String] = rdd.map(
      record => Parser.toStr(record.get("i_server_ip"))
    )
    val vertexRDD: RDD[String] = vertexSrc.union(vertexDst).distinct()
    val vertex: RDD[(VertexId, String)] = vertexRDD.map(
      record => new Tuple2(Parser.ip2Long(record), record)
    )
    val edge: RDD[Edge[String]] = rdd.map(
      record =>
        new Edge[String](
          Parser.ip2Long(Parser.toStr(record.get("i_client_ip"))),
          Parser.ip2Long(Parser.toStr(record.get("i_server_ip"))),
          Parser.toStr(record.get("@timestamp"), record.get("i_server_port")
      ))
    )
    val graph: Graph[String, String] = Graph(vertex, edge)
    return graph
  }

  def graphOutDegreeAvg (graph: Graph[String, String]) : Long = {
    if (graph == null)
      return 0
    val nodes : Long = graph.vertices.count()
    val outDegree : Long = graph.outDegrees.map(line => line._2).reduce((r1, r2) => r1 + r2)
    if (nodes == 0)
      return 0
    outDegree / nodes
  }

  def graphInDegreeAvg (graph : Graph[String, String]) : Long = {
    if (graph == null)
      return 0
    val nodes : Long = graph.vertices.count()
    val inDegree : Long  = graph.inDegrees.map(line => line._2).reduce((r1, r2) =>  r1 + r2)
    if (nodes == 0)
      return 0
    inDegree / nodes
  }

  private def getOutDegree (graph: Graph[String, String]) : RDD[(VertexId, Int)] = {
    graph.outDegrees
  }

  def getOutDegreeOverList (graph: Graph[String, String], least : Int, result : util.HashMap[Long, Int], sparkSession: SparkSession) : Unit = {
    var result : util.HashMap[Long, Int] = new util.HashMap[Long, Int]()
    if (graph == null)
      return
    if (least < 1)
      return
    if (sparkSession == null)
      return
    val outDegreeRDD: RDD[(VertexId, Int)] = getOutDegree(graph)
    if (outDegreeRDD == null)
      return
    import sparkSession.implicits._
    val outDegreeDataFrame : DataFrame = outDegreeRDD.map(
      record => (record._1 , record._2)
    ).toDF()
    outDegreeDataFrame.createOrReplaceTempView("outDegree")
    outDegreeDataFrame
    val outDegreeResultDataFrame : DataFrame = sparkSession.sql(
      "SELECT vertexID, counts FROM outDegree WHERE counts > " + least
    )
    val resultArray : Array[Map[String, Any]] = outDegreeResultDataFrame.map(
      record => record.getValuesMap[Any](List("vertexId", "counts"))
    ).collect()
    resultArray.foreach(
      resultSingle => {result.put(Parser.toLong(resultSingle.get("vertexId")), Parser.toInt(resultSingle.get("counts")))}
    )
    println(result.size())
  }
}
